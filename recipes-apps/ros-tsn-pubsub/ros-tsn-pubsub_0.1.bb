# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "ROS 2 publisher/subscriber example for the TSN multi-node demo"
DESCRIPTION = "Builds the xlnx-pubsub ament package from Xilinx/ros-tsn-pubsub. \
Provides the talker (pub-control) and listener (sub-all) executables used to \
exercise the ROS 2 multi-node-communication-via-TSN application on Kria \
KR260/KD240, the FastRTPS DDS talker/listener profiles and the \
start_ros_test.sh launch helper."
SECTION = "apps"

HOMEPAGE = "https://github.com/Xilinx/ros-tsn-pubsub"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41bcdd6af856cc34f13217eb319564a3"

SRC_BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/ros-tsn-pubsub.git;branch=${SRC_BRANCH};protocol=https"
SRCREV = "29d9cfb22f55a8834d3737073bfa18450bab399c"

PV = "0.1.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit ros_distro_jazzy
inherit ros_component

ROS_BPN = "xlnx-pubsub"

ROS_BUILD_DEPENDS = " \
    rclcpp \
    std-msgs \
"

ROS_BUILDTOOL_DEPENDS = " \
    ament-cmake-native \
"

ROS_EXPORT_DEPENDS = ""
ROS_BUILDTOOL_EXPORT_DEPENDS = ""

# start_ros_test.sh drives `ros2 run xlnx-pubsub {talker,listener}`, so the
# ros2 CLI run verb has to be on the target alongside the executables.
# ros-workspace ships the top-level ROS 2 environment (/opt/ros/jazzy/
# setup.bash) that the operator sources before launching the app; it is not
# pulled in transitively by rclcpp/ros2run, so pull it explicitly here.
ROS_EXEC_DEPENDS = " \
    rclcpp \
    ros2run \
    ros-workspace \
    std-msgs \
"

DEPENDS = " \
    ${ROS_BUILD_DEPENDS} \
    ${ROS_BUILDTOOL_DEPENDS} \
    ${ROS_BUILDTOOL_EXPORT_DEPENDS} \
    ${ROS_EXPORT_DEPENDS} \
"

RDEPENDS:${PN} += "bash ${ROS_EXEC_DEPENDS}"

ROS_BUILD_TYPE = "ament_cmake"
inherit ros_${ROS_BUILD_TYPE}

# The example CMakeLists installs start_ros_test.sh under SCRIPT_PREFIX
# (/opt/xilinx/ros-tsn-example/bin), which sits outside ${ros_prefix}, so the
# base ros_component FILES do not cover it. Package it explicitly.
FILES:${PN} += "/opt/xilinx/ros-tsn-example"
