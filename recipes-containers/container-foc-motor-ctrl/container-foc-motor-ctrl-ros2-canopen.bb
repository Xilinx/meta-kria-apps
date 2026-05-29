# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria FOC motor-control ROS2 CANopen OCI container image"
DESCRIPTION = "OCI container image targeting Kria KR260 (and other non-\
KD240 boards), intended to run the foc-motor-ctrl ROS2 CANopen \
client against a KD240 server. Bundles the kr260-tsn-rs485pmod \
firmware overlay used for the CAN/RS485 PMOD on KR260, the \
kria-motor-control ament package (ros2_canopen / ros2_control launch \
files and config + URDF + pre-generated DCFs), the EDF ROS 2 canopen \
and ros2_control package sets, and the can-utils CLI for operator-side \
link checks. Developer tooling (ament-cmake-*, colcon, rosdep, gdb, \
vcan, package management, ...) lives in the sibling \
container-foc-motor-ctrl-ros2-canopen-dev container."

require recipes-containers/kria-app-container.inc

# Leave the OCI entrypoint empty so the operator launches `ros2 launch
# ...` (or a shell) explicitly. See container-foc-motor-ctrl-server.bb
# for the rationale.
CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = ""

# kria-motor-control's ROS_EXEC_DEPENDS already lists the runtime
# canopen / controller-manager / xacro / robot-state-publisher set,
# so installing just the ament package pulls the canopen client +
# 402 driver + master driver transitively. Layer
# packagegroup-xilinx-ros-control on top as the EDF reference does -
# it pulls the full ros2-control + controller-interface +
# hardware-interface + ros2controlcli set, a known-good superset of
# what the launch flow needs. can-utils gives cangen / candump /
# cansend for field debugging of the CAN link.
#
# Note: the EDF reference also pulled packagegroup-xilinx-ros-canopen,
# but that packagegroup was dropped in the 2026.1-rlatest refresh of
# meta-xilinx-demos. The runtime canopen closure is now driven from
# kria-motor-control's RDEPENDS instead. Once on-hardware launch is
# verified end-to-end, evaluate whether the -control packagegroup can
# also be trimmed in favour of explicit RDEPENDS on the ament package.
CONTAINER_APP = " \
    kr260-tsn-rs485pmod-fw \
    kria-motor-control \
    packagegroup-xilinx-ros-control \
    can-utils \
"
