# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Out-of-tree AR1335 image sensor V4L2 subdev driver"
DESCRIPTION = "Kernel module providing a V4L2 subdevice driver for the \
onsemi AR1335 13MP image sensor used on Kria carrier cameras."
HOMEPAGE = "https://github.com/Xilinx/ar1335-module"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
# LICENSE lives at the repo root, while sources are under src/. Reference it
# relative to the unpacked tree so do_package can stage it without warnings.
LIC_FILES_CHKSUM = "file://../LICENSE;md5=7aaf58a37e0aa851bb718d75c1734884"

# Upstream has no tags; pin to a known-good main commit.
SRC_BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/ar1335-module.git;protocol=https;branch=${SRC_BRANCH}"
SRCREV = "df0996900eebbe8d6e4bd801977aa34a6d91322d"
PV = "0.1+git${SRCPV}"

S = "${WORKDIR}/git/src"

inherit module

INHIBIT_PACKAGE_STRIP = "1"

