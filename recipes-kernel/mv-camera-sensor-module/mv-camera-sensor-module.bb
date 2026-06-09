# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Out-of-tree FRAMOS IMX547 (Sony Pregius S) V4L2 subdev driver"
DESCRIPTION = "Kernel module providing a V4L2 subdevice driver for the \
FRAMOS IMX547 (Sony Pregius S) machine-vision camera sensor used on \
Kria carrier boards."
HOMEPAGE = "https://github.com/Xilinx/mv-camera-sensor-module"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE.md;md5=5f09740fd2d17408528aaf480de6353c"

SRC_BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/mv-camera-sensor-module.git;protocol=https;branch=${SRC_BRANCH}"
SRCREV = "291fb1991a6b7d30ee0575acccb4c65be5629a97"
PV = "0.3+git${SRCPV}"

S = "${WORKDIR}/git/src"

inherit module

INHIBIT_PACKAGE_STRIP = "1"

RDEPENDS:${PN} += "kernel-module-imx547"
