# meta-kria-apps

This layer provides recipes to build and deploy Kria applications for the
AMD EDF 2026.1 release, targeting Yocto Scarthgap.

## Supported Recipes and Target boards


## Dependencies

This layer depends on:

    URI: https://git.openembedded.org/openembedded-core
    layers: meta

    URI: https://github.com/Xilinx/meta-kria
    branch: 2026.1

## Maintainers / Patches / Submissions

Please open pull requests against the `edf-2026.1` branch.

Follow OE community patch submission guidelines:
  https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines

Patch format:
  git format-patch -s --subject-prefix="meta-kria-apps][edf-2026.1][PATCH" HEAD~1

**Maintainer:**

    Pallav Joshi <pallav.joshi@amd.com>
