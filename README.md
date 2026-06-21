# jASN1 archive

This repository is archived.

It was originally created to preserve and compare the historical releases of **jASN1**, a Java ASN.1 BER encoding/decoding library formerly maintained by OpenMUC.

The goal of this repository was not to become the upstream project, but to provide a convenient Git history for old jASN1 releases, especially before the project history was reorganized.

## Current upstream project

The jASN1 project is now maintained by Beanit under the name **ASN1bean**.

Use the Beanit project for any new development:

- Project website: https://www.beanit.com/asn1/
- GitHub repository: https://github.com/beanit/asn1bean
- Maven artifact: `com.beanit:asn1bean`

Beanit's repository does not contain the full pre-1.8.1 history that was preserved here, so this repository remains available as a historical archive.

## Maven coordinates history

The Maven coordinates changed over time:

| Versions | Group ID | Artifact ID | Notes |
|---|---|---|---|
| `1.12` → current | `com.beanit` | `asn1bean` | Current Beanit artifact |
| `1.11` → `1.11.3` | `com.beanit` | `jasn1` | Beanit releases using the old artifact name |
| `1.4` → `1.10` | `org.openmuc` | `jasn1` | Historical OpenMUC releases |

For new projects, use the current ASN1bean artifact:

```xml
<dependency>
    <groupId>com.beanit</groupId>
    <artifactId>asn1bean</artifactId>
    <version><!-- use the latest version from Maven Central --></version>
</dependency>
```

Or with Gradle:

```kotlin
implementation("com.beanit:asn1bean:<version>")
```

Check Maven Central for available versions:

- `com.beanit:asn1bean`: https://central.sonatype.com/artifact/com.beanit/asn1bean/versions
- `com.beanit:jasn1`: https://central.sonatype.com/artifact/com.beanit/jasn1/versions
- `org.openmuc:jasn1`: https://central.sonatype.com/artifact/org.openmuc/jasn1/versions

## Branching strategy

This repository uses two main branches:

- `master`: reconstructed Git history used to preserve and compare historical jASN1 releases.
- `mainstream`: upstream release snapshots.

The `mainstream` branch contains the upstream source trees as they were published:

- for the historical OpenMUC releases, from the release `.tgz` archives;
- for version `1.8.1` and up to `1.11.3`, from the Beanit repository.

This makes it possible to compare the reconstructed history with the upstream release contents and to keep this archive traceable to the original published artifacts.

## Historical notes

This repository was created by Julien Herr around 2014 to make it easier to inspect the differences between jASN1 versions.

It is not the official upstream project and should not be used for active maintenance, bug reports, or new feature requests.

For active development, issues, documentation, and releases, refer to ASN1bean:

https://github.com/beanit/asn1bean
