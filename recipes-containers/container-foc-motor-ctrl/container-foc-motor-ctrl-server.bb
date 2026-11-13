# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria FOC motor-control server OCI container image"
DESCRIPTION = "OCI container image targeting Kria KD240, bundling the \
kd240-motor-ctrl-qei firmware overlay, the IIO motor-control kernel \
modules, the foc-motor-ctrl CANopen server (fmc_canopen) and the \
Bokeh-based dashboard, both started with start_motor_control, and the \
can-utils CLI for operator-side link checks."

require recipes-containers/kria-app-container.inc

# Leave the OCI entrypoint empty. The operator launches the server,
# dashboard, or a shell explicitly via `docker run -it <image> <cmd>`;
# having no fixed entrypoint avoids the gotcha where `docker run
# <image> ls` would otherwise become `bash ls` and be misinterpreted.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = ""

# kd240-motor-ctrl-qei-fw already RDEPENDS motor-control-iio-modules
# (see recipes-firmware/module-dependencies.md), so the full IIO driver
# set lands transitively. foc-motor-ctrl-server / -dashboard RDEPEND
# the foc-motor-ctrl lib + python binding, so the lib lands transitively
# too. Only list the top-level recipes the container actually drives.
#
# lmsensors-sensors provides the `sensors` CLI used to read the 1-Wire
# thermal hwmon inputs (w1_therm) bound by the kd240-motor-ctrl-qei
# overlay.
CONTAINER_APP = " \
    kd240-motor-ctrl-qei-fw \
    foc-motor-ctrl-server \
    foc-motor-ctrl-dashboard \
    lmsensors-sensors \
    can-utils \
"
