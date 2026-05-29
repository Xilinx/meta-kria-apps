# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria FOC motor-control ROS2 CANopen OCI container, dev flavour"
DESCRIPTION = "Developer-oriented sibling of \
container-foc-motor-ctrl-ros2-canopen. Adds the full ament-cmake-* / \
colcon / rosdep / rosidl / gtest tool-chain via \
packagegroup-xilinx-ros-devtools, kernel-module-vcan for ros2_canopen \
loopback testing without real hardware, and the EDF dev-tools base \
(dev-pkgs, tools-sdk, package-management, vim-tiny, git, \
ca-certificates). Intended for on-device development and debugging, \
not for production deployment."

# Inherit everything (firmware overlay, kria-motor-control, can-utils,
# base userspace) from the runtime container, then layer the EDF
# devtools base on top. Mirrors the meta-amd-edf pattern in
# recipes-containers/examples/container-ros2-canopen-control-dev.bb.
require recipes-containers/container-foc-motor-ctrl/container-foc-motor-ctrl-ros2-canopen.bb
require recipes-extended/images/container-devtools-edf.bb

# packagegroup-xilinx-ros-dev brings the ament-cmake-* / colcon /
# rosdep / rosidl / gtest tool-chain expected by on-device ROS 2
# development. kernel-module-vcan covers ros2_canopen loopback testing
# without the real CAN PMOD. Anything else dev-leaning (vim, git,
# ca-certificates, package-management, dev-pkgs, tools-sdk) is pulled
# in via the container-devtools-edf base required above.
CONTAINER_APP:append = " \
    packagegroup-xilinx-ros-dev \
    kernel-module-vcan \
"
