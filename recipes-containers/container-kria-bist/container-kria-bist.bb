# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria BIST OCI container image"
DESCRIPTION = "OCI container image bundling the kria-bist pytest suite \
together with the per-board firmware overlays it loads at runtime to \
exercise the KD240, KR260 and KV260 carrier interfaces. Each *-bist-fw \
recipe RDEPENDs on its own kernel-module / IIO driver set, so listing \
only the firmware names here pulls the full driver stack in."

require recipes-containers/kria-app-container.inc

# Leave the OCI entrypoint empty so the operator runs commands (BIST
# suite, dfx-fw-install, a shell) explicitly. A non-empty entrypoint
# ("bash") gets PREPENDED to whatever the user passes, so
# `docker run <image> sh -c "..."` would become `bash sh -c "..."` and
# fail with "cannot execute binary file". Matches the foc / tsn
# containers, which all use an empty CONTAINER_APP_CMD.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = ""

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
