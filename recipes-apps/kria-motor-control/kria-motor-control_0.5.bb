# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "ROS 2 CANopen client for the Kria FOC motor-control server"
DESCRIPTION = "Builds the kria_motor_control ament package from \
foc-motor-ctrl/test/ros2_canopen. The package provides the ros2_control / \
ros2_canopen configuration and launch files used on KR260 to drive the FOC \
motor server running on KD240 over CAN."
HOMEPAGE = "https://github.com/Xilinx/foc-motor-ctrl"
SECTION = "devel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=370aacba9d9e1a362c89a2b36dc0608d"

inherit ros_distro_jazzy
inherit ros_component

ROS_BPN = "kria_motor_control"

ROS_BUILD_DEPENDS = " \
    canopen \
    lely-core-libraries \
"

ROS_BUILDTOOL_DEPENDS = " \
    ament-cmake-native \
"

ROS_EXPORT_DEPENDS = ""
ROS_BUILDTOOL_EXPORT_DEPENDS = ""

ROS_EXEC_DEPENDS = " \
    canopen-402-driver \
    canopen-core \
    canopen-master-driver \
    canopen-ros2-control \
    canopen-ros2-controllers \
    controller-manager \
    forward-command-controller \
    joint-state-broadcaster \
    joint-trajectory-controller \
    robot-state-publisher \
    velocity-controllers \
    xacro \
"

DEPENDS  = "${ROS_BUILD_DEPENDS} ${ROS_BUILDTOOL_DEPENDS}"
DEPENDS += "${ROS_EXPORT_DEPENDS} ${ROS_BUILDTOOL_EXPORT_DEPENDS}"
DEPENDS += " \
    lely-core-libraries-native \
    lely-core-native \
"

RDEPENDS:${PN} += "${ROS_EXEC_DEPENDS}"

# Stay in lockstep with foc-motor-ctrl_0.5.bb (v0.5 tag).
SRC_URI = " \
    git://github.com/Xilinx/foc-motor-ctrl.git;protocol=https;branch=main \
    file://0001-package-bump-version-to-0.5.0.patch;striplevel=3 \
"
SRCREV = "18efd3c9e6689281f357193239c358b7a20785a2"

S = "${WORKDIR}/git/test/ros2_canopen"

ROS_BUILD_TYPE = "ament_cmake"
inherit ros_${ROS_BUILD_TYPE}

do_compile:prepend() {
    export PATH="${RECIPE_SYSROOT_NATIVE}${ros_bindir}:$PATH"
}

# config/**/foc-mc.eds is shipped as a relative symlink pointing outside the
# ament install tree (../../../../apps/foc-mc.eds). After install the target
# does not exist, leaving dangling symlinks. Replace each link with the real
# EDS file from the source tree.
do_install:append() {
    for f in $(find ${D}${ros_datadir}/${ROS_BPN}/config -name "foc-mc.eds" -type l 2>/dev/null); do
        rm -f "$f"
        install -m 0644 ${WORKDIR}/git/apps/foc-mc.eds "$f"
    done
}
