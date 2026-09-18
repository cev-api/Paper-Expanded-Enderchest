Paper [![Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory.papermc.io%2Fartifactory%2Funiverse%2Fio%2Fpapermc%2Fpaper%2Fpaper-api%2Fmaven-metadata.xml&strategy=highestVersion&filter=26.3.*&label=version&color=%23344ceb
)](https://papermc.io/downloads/paper)
[![Paper Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=main)](https://github.com/PaperMC/Paper/actions)
[![Discord](https://img.shields.io/discord/289587909051416579.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/papermc)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/papermc?label=GitHub%20Sponsors)](https://github.com/sponsors/PaperMC)
[![Open Collective](https://img.shields.io/opencollective/all/papermc?label=OpenCollective%20Sponsors)](https://opencollective.com/papermc)
===========

The most widely used, high-performance Minecraft server that aims to fix gameplay and mechanics inconsistencies.

---

## ⚠️ Downstream fork: expanded ender chest

> **This repository is an unofficial downstream fork of Paper and is not an official PaperMC build.**
> It tracks upstream [PaperMC/Paper](https://github.com/PaperMC/Paper) for Minecraft **26.2** and adds
> exactly one feature, kept on the [`expanded-enderchest`](https://github.com/cev-api/Paper-Expanded-Enderchest/tree/expanded-enderchest) branch.

### Configurable native ender chest capacity

The size of a player's ender chest is configurable. Add the setting to `config/paper-global.yml`:

```yaml
misc:
  ender-chest-slot-count: 27
```

| Setting | Default | Valid values |
| --- | --- | --- |
| `misc.ender-chest-slot-count` | `27` | multiples of `9` between `9` and `54` (`9`, `18`, `27`, `36`, `45`, `54`) |

Values outside that set are rounded to the nearest supported size and logged as a warning.
The configured size is applied when a player's ender chest is created, so **restart the server** for a
change to take effect.

#### How it works

- The player's real `PlayerEnderChestContainer` is resized itself, so its slot count **is** the configured size.
- Opening a physical ender chest opens that exact same container, wrapped in a native chest menu with the
  matching number of rows: `9` → 1 row, `18` → 2 rows, … `54` → 6 rows.
- There is exactly **one authoritative ender chest inventory** per player. This is not a virtual `/ec`
  inventory, not a plugin, not a database-backed inventory, and not a copy-on-open / copy-on-close
  synchronisation system. Nothing depends on `InventoryCloseEvent` to commit item state.
- Existing 27-slot ender chest data stays compatible: the first 27 slots keep their contents.

> ⚠️ **Increasing** the configured size is safe and simply adds usable slots. **Decreasing** it makes the
> contents of the removed slots unreadable, and they are dropped the next time that player's data is saved.
> Move items out of the high slots before reducing the size.

### Precompiled builds and automatic updates

The [releases page](https://github.com/cev-api/Paper-Expanded-Enderchest/releases) contains precompiled Paperclip jars.
A scheduled workflow rebases this feature onto upstream Paper 26.2 every six hours, rebuilds the server
and publishes a new release, with no input needed.

If Paper moves to a different Minecraft version the automation **stops on purpose** instead of attempting a
cross-version port. It raises a single issue titled `Manual port required: …` containing the port checklist and
then goes quiet, rather than failing every six hours. Nothing is rebased, pushed or released, so the last
working build stays downloadable. Once the feature has been ported and a run succeeds, the issue is closed
automatically.

> A manual **Run workflow** always fails loudly when the Minecraft version does not match, so an intentional
> stop can never be mistaken for a successful update.

Downloads are named the way Paper names its own, with the build number straight after the
Minecraft version and our fork appended:

| Download | Meaning |
| --- | --- |
| `paper-26.2-124-expanded-enderchest.jar` | upstream Paper build 124 contains this code; the release notes give the exact commit |
| `paper-26.2-124+-expanded-enderchest.jar` | newer than build 124 — contains all of it plus upstream commits that have no build yet |

The `expanded-enderchest-latest` release always points at the newest build and its asset is
named the same way, so a download link taken from it always says which build it is.

The same number is compiled into the server. It names the intermediate artefact
`paper-paperclip-26.2.build.124-stable.jar` and is reported on startup as
`Paper 26.2-124-expanded-enderchest`. Release tags stay commit based, so a build is never
overwritten by a later one.

---

**Support and Project Discussion:**
- [Our forums](https://forums.papermc.io/) or [Discord](https://discord.gg/papermc)

How To (Server Admins)
------
Paperclip is a jar file that you can download and run just like a normal jar file.

Download Paper from our [downloads page](https://papermc.io/downloads/paper).

Run the Paperclip jar directly from your server. Just like old times.

* Documentation on using Paper: [docs.papermc.io](https://docs.papermc.io)
* For a sneak peek at upcoming features, [see here](https://github.com/PaperMC/Paper/projects)

How To (Plugin Developers)
------
* See our API [here](paper-api)
* See upcoming, pending, and recently added API [here](https://github.com/orgs/PaperMC/projects/2/views/4)
* Paper API javadocs here: [papermc.io/javadocs](https://papermc.io/javadocs/)
#### Repository (for paper-api)
See [the docs](https://docs.papermc.io/paper/dev/project-setup/#adding-paper-as-a-dependency) for more details.
##### Gradle
```kotlin
repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
```
##### Maven

```xml
<repository>
    <id>papermc</id>
    <url>https://repo.papermc.io/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>[26.3.build,)</version>
    <scope>provided</scope>
</dependency>
```

How To (Compiling Jar From Source)
------
To compile Paper, you need JDK 25 and an internet connection.

Clone this repo, run `./gradlew applyPatches`, then `./gradlew createPaperclipJar` from your terminal. You can find the compiled jar in the `paper-server/build/libs` directory.

To get a full list of tasks, run `./gradlew tasks`.

How To (Pull Request)
------
See [Contributing](CONTRIBUTING.md)

Old Versions (1.21.3 and below)
------
For branches of versions 1.8-1.21.3, please see our [archive repository](https://github.com/PaperMC/Paper-archive).

Support Us
------
First of all, thank you for considering helping out, we really appreciate that!

PaperMC has various recurring expenses, mostly related to infrastructure. Paper uses [Open Collective](https://opencollective.com/) via the [Open Source Collective fiscal host](https://opencollective.com/opensource) to manage expenses. Open Collective allows us to be extremely transparent, so you can always see how your donations are used. You can read more about financially supporting PaperMC [on our website](https://papermc.io/sponsors).

You can find our collective [here](https://opencollective.com/papermc), or you can donate via GitHub Sponsors [here](https://github.com/sponsors/PaperMC), which will also go towards the collective.

Special Thanks To:
-------------

[![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/)

[YourKit](https://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler) and [.NET](https://www.yourkit.com/.net/profiler) application profilers. We thank them for granting Paper an OSS license so that we can make our software the best it can be.

All our sponsors!  
[![Sponsor Image](https://raw.githubusercontent.com/PaperMC/papermc.io/data/sponsors.png)](https://papermc.io/sponsors)
