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
    gstreamer1.0-plugins-base-meta \
    gstreamer1.0-plugins-good-meta \
    gstreamer1.0-plugins-bad-meta \
    gst-perf \
    libdrm-tests \
    iperf3 \
    ethtool \
    mtd-utils \
    i2c-tools \
    freeipmi \
    iputils-ping \
    util-linux-mount \
    util-linux-umount \
    libtss2-tcti-device \
"

KRIA_BIST_INSTALL_DIR = "/opt/xilinx/kria-bist"

do_install() {
    install -d ${D}${KRIA_BIST_INSTALL_DIR}
    cp -r ${S}/tests ${D}${KRIA_BIST_INSTALL_DIR}/

    # tpm2-tools defaults its TCTI to swtpm (TCP 127.0.0.1:2321), which
    # is not running in the container and is not what we want anyway -
    # the BIST suite drives the real on-board TPM via the kernel
    # character device. Pin the default TCTI to the in-kernel resource
    # manager so plain "tpm2_pcrread" / "tpm2_getcap" calls just work.
    install -d ${D}${sysconfdir}/profile.d
    cat > ${D}${sysconfdir}/profile.d/kria-bist-tpm2.sh <<'EOF'
# Set by the kria-bist recipe so tpm2-tools targets the kernel TPM
# resource manager instead of falling back to swtpm.
if [ -e /dev/tpmrm0 ]; then
    export TPM2TOOLS_TCTI="device:/dev/tpmrm0"
    export TCTI="device:/dev/tpmrm0"
elif [ -e /dev/tpm0 ]; then
    export TPM2TOOLS_TCTI="device:/dev/tpm0"
    export TCTI="device:/dev/tpm0"
fi
EOF
    chmod 0644 ${D}${sysconfdir}/profile.d/kria-bist-tpm2.sh
}

FILES:${PN} = " \
    ${KRIA_BIST_INSTALL_DIR} \
    ${sysconfdir}/profile.d/kria-bist-tpm2.sh \
"
