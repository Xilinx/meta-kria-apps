# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria BIST OCI container image"
DESCRIPTION = "OCI container image bundling the kria-bist pytest suite \
together with the per-board firmware overlays it loads at runtime to \
exercise the KD240, KR260 and KV260 carrier interfaces. Each *-bist-fw \
recipe RDEPENDs on its own kernel-module / IIO driver set, so listing \
only the firmware names here pulls the full driver stack in."

require recipes-containers/kria-app-container.inc

# Userspace entrypoint is bash for now; switch to the actual BIST CLI
# once it lands as a script in the kria-bist recipe.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = "bash"

# Per-board firmware overlays plus the kria-bist test suite. Kernel
# module dependencies (motor-control-iio-modules, mcp251x, ap1302,
# xlnx-vcu, ar1335-module, etc.) are pulled in transitively via each
# firmware recipe's RDEPENDS, so they do not need to be listed here.
CONTAINER_APP = " \
    kria-bist \
    foc-motor-ctrl \
    kd240-bist-fw \
    kr260-bist-fw \
    kv260-bist-fw \
"
