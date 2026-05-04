# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Helper to modprobe Kria firmware-overlay kernel modules from inside an OCI container"
DESCRIPTION = "kria-modules-load reads /usr/lib/modules-load.d/<fw>.conf \
(shipped by the matching *-fw recipe) and modprobes each module so that \
the kernel has the right drivers registered when dfx-mgr loads the \
corresponding bitstream + DT overlay. Provides the equivalent of \
systemd-modules-load.service for OCI containers that don't run systemd."
HOMEPAGE = "https://github.com/Xilinx/meta-kria-apps"
SECTION = "utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://kria-modules-load"

S = "${WORKDIR}"

inherit allarch

# kmod for modprobe/rmmod, coreutils for the awk/tac the script uses.
RDEPENDS:${PN} = "kmod coreutils"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/kria-modules-load ${D}${bindir}/kria-modules-load
}

FILES:${PN} = "${bindir}/kria-modules-load"
