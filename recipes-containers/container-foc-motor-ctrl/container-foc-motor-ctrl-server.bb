# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria FOC motor-control server OCI container image"
DESCRIPTION = "OCI container image targeting Kria KD240, bundling the \
kd240-motor-ctrl-qei firmware overlay and the IIO motor-control kernel \
modules required by the FOC server / Bokeh dashboard. The userspace \
server (foc-motor-ctrl + dashboard) will be added via a bbappend in a \
follow-up."

require recipes-containers/kria-app-container.inc

CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = "bash"

# motor-control-iio-modules is the recipe wrapper that RDEPENDs on all the
# per-module kernel-module-* packages, so listing it here installs the full
# IIO motor-control driver set used by the FOC server.
CONTAINER_APP = " \
    kd240-motor-ctrl-qei-fw \
    motor-control-iio-modules \
    foc-motor-ctrl-server \
    foc-motor-ctrl-dashboard \
"
