# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Out-of-tree IIO kernel modules for FOC motor control"
DESCRIPTION = "IIO framework drivers for the Kria KD240 motor control \
subsystem: ADC hub, FOC periodic, QEI, PWM generator, and SVPWM duty."
HOMEPAGE = "https://github.com/Xilinx/motor-control-iio-modules"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
# LICENSE lives at the repo root, while sources are under src/. Reference it
# relative to the unpacked tree so do_package can stage it without warnings.
LIC_FILES_CHKSUM = "file://../LICENSE;md5=7d3d4519e1b227873d840232a002e048"

SRC_BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/motor-control-iio-modules.git;protocol=https;branch=${SRC_BRANCH}"
SRCREV = "3900e31dce144199c42a528bbce93ede2946a690"

S = "${WORKDIR}/git/src"

inherit module

INHIBIT_PACKAGE_STRIP = "1"

