# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria Board Interface Self-Test (BIST) suite"
DESCRIPTION = "Python pytest-based test suite that validates the on-board \
interfaces of Kria SOM carrier boards (KD240, KR260, KV260)."
HOMEPAGE = "https://github.com/Xilinx/kria-bist"
SECTION = "tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=373bd34d54f1879c43e6c180077e7eec"

SRC_URI = "git://github.com/Xilinx/kria-bist.git;branch=main;protocol=https"
SRCREV = "ab58af6c626149f722add57298df8516f58ba705"

S = "${WORKDIR}/git"

inherit allarch

# Userland runtime: pytest test runner plus all the helpers the suite drives
# (CAN, IIO, GPIO, EEPROM/I2C, video, network, storage, TPM and IPMI tools).
RDEPENDS:${PN} = " \
    python3-pytest \
    python3-periphery \
    python3-can \
    python3-pymodbus \
    python3-netifaces \
    libiio \
    libiio-python3 \
    tpm2-tools \
    v4l-utils \
    gstreamer1.0 \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    libdrm-tests \
    iperf3 \
    ethtool \
    mtd-utils \
    i2c-tools \
    freeipmi \
    util-linux-mount \
    util-linux-umount \
"

KRIA_BIST_INSTALL_DIR = "/opt/xilinx/kria-bist"

do_install() {
    install -d ${D}${KRIA_BIST_INSTALL_DIR}
    cp -r ${S}/tests ${D}${KRIA_BIST_INSTALL_DIR}/
}

FILES:${PN} = "${KRIA_BIST_INSTALL_DIR}"
