# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.
#
# Shared SRC_URI / SRCREV for every recipe that builds out of the
# foc-motor-ctrl upstream tree (foc-motor-ctrl and kria-motor-control).
# Pinning both recipes through this single file keeps them in lockstep
# so the ROS 2 client (kria_motor_control package.xml + launch files)
# can never drift from the native lib / server / dashboard build.
#
# LIC_FILES_CHKSUM stays in the per-recipe .bb because S differs
# between consumers and the relative path to LICENSE changes accordingly.

HOMEPAGE = "https://github.com/Xilinx/foc-motor-ctrl"
LICENSE  = "MIT"

SRC_BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/foc-motor-ctrl.git;branch=${SRC_BRANCH};protocol=https"
SRCREV  = "18efd3c9e6689281f357193239c358b7a20785a2"
