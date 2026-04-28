# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria BIST OCI container image"
DESCRIPTION = "OCI container image bundling all firmware overlays and \
kernel modules required to run the Kria Built-In Self-Test (BIST) suite \
across KD240, KR260 and KV260. Userspace BIST tooling will be added via \
a bbappend in a follow-up."

require recipes-extended/images/container-app-base.bb

# Userspace entrypoint is a placeholder; the actual BIST CLI will be set
# once the kria-bist app recipe is integrated.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = "bash"

# Firmware overlays for every board the BIST container is expected to
# run on, plus the IIO modules used by the KD240 motor-control BIST.
# motor-control-iio-modules is the recipe wrapper that RDEPENDs on the
# per-module kernel-module-* packages, so pulling it in here installs the
# whole IIO motor-control driver set required by the KD240 BIST.
CONTAINER_APP = " \
    kd240-bist-fw \
    kr260-bist-fw \
    kv260-bist-fw \
    motor-control-iio-modules \
"
