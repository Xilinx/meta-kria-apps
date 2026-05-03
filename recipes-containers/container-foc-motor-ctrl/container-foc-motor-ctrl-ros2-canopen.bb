# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria FOC motor-control ROS2 CANopen OCI container image"
DESCRIPTION = "OCI container image targeting Kria KR260 (and other non-\
KD240 boards), intended to run the foc-motor-ctrl ROS2 CANopen \
host/client against a KD240 server. Bundles the kr260-tsn-rs485pmod \
firmware overlay used for the CAN/RS485 PMOD on KR260; the userspace \
ROS2 stack will be added via a bbappend in a follow-up."

require recipes-containers/kria-app-container.inc

CONTAINER_SHELL = "bash"
CONTAINER_APP_CMD = "bash"

CONTAINER_APP = " \
    kr260-tsn-rs485pmod-fw \
"
