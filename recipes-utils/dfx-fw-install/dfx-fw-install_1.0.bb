# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Helper to copy a Kria firmware directory out of an OCI container onto the host"
DESCRIPTION = "dfx_fw_install runs inside a Kria app OCI container and \
rsyncs /lib/firmware/xilinx/<fw-name>/ contents into a bind-mounted host \
directory. Required because the host's dfx-mgr daemon can only load \
firmware from host-visible paths under /lib/firmware/xilinx/."
HOMEPAGE = "https://github.com/Xilinx/meta-kria-apps"
SECTION = "utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://dfx_fw_install"

S = "${WORKDIR}"

inherit allarch

# rsync for content-aware copy / dry-run; coreutils for sync(1) and the
# POSIX flag set the script relies on.
RDEPENDS:${PN} = "rsync coreutils"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/dfx_fw_install ${D}${bindir}/dfx_fw_install
}

FILES:${PN} = "${bindir}/dfx_fw_install"
