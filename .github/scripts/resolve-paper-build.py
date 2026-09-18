#!/usr/bin/env python3
"""Work out which published PaperMC build an upstream commit belongs to.

Paper publishes its downloads as ``paper-<minecraft>-<build>.jar``. The build API returns every
published build together with the commits that build introduced, newest first, so an upstream
commit can be mapped back to the build that contains it.

Prints ``number=``, ``label=`` and ``note=`` lines on stdout for the workflow to read. It never
fails: if anything goes wrong the values come back empty and the caller falls back to naming the
artefact after the upstream commit instead.
"""

import json
import sys
import urllib.request

API = "https://fill.papermc.io/v3/projects/paper/versions/{mc}/builds"


def resolve(builds, sha):
    """Return (build number, kind, human readable note) for an upstream commit."""
    containing = [b for b in builds if any(c.get("sha") == sha for c in b.get("commits", []))]
    if not containing:
        newest = builds[0]["id"]
        return newest, "plus", "newer than every published build, contains all of build {}".format(newest)
    build = containing[0]
    if build["commits"][0].get("sha") == sha:
        return build["id"], "exact", "is upstream Paper build {}".format(build["id"])
    # Our commit is one of the commits this build introduced, so this is the build it
    # first shipped in. The exact commit is always recorded in the release notes.
    return build["id"], "contains", "is upstream Paper build {}, up to commit {}".format(build["id"], sha[:7])


def main(argv):
    if len(argv) != 3:
        print("usage: resolve-paper-build.py <minecraft-version> <upstream-commit>", file=sys.stderr)
        return 2

    mc, sha = argv[1], argv[2]
    number, label, note = "", "", "no upstream build number resolved"

    try:
        request = urllib.request.Request(API.format(mc=mc), headers={"User-Agent": "expanded-enderchest-fork"})
        with urllib.request.urlopen(request, timeout=30) as response:
            builds = json.load(response)
        if builds:
            number, kind, note = resolve(builds, sha)
            # "+" means we carry newer upstream commits that have no build yet.
            label = "{}+".format(number) if kind == "plus" else str(number)
        else:
            note = "the build API returned no builds for Minecraft {}".format(mc)
    except Exception as error:  # any failure just means "no build number"
        print("warning: could not resolve the upstream Paper build number: {}".format(error), file=sys.stderr)

    print("number={}".format(number))
    print("label={}".format(label))
    print("note={}".format(note))
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))
