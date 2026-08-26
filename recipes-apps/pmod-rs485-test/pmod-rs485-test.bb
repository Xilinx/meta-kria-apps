# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Kria PMOD RS-485 loopback / Modbus self-test"
DESCRIPTION = "Small C utility used to exercise the RS-485 PMOD on Kria \
KR260 via libmodbus. Installed under /opt/xilinx/pmod-rs485-test/bin."
HOMEPAGE = "https://github.com/Xilinx/pmod-rs485-test"
SECTION = "tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5c9f774c9491b577f954e84fc7ec0007"

SRC_URI = "git://github.com/Xilinx/pmod-rs485-test.git;branch=main;protocol=https"
# v0.2 release tag (commit 1bbcdd8). SRCREV is pinned to the immutable
# commit for reproducibility; the tag is noted here for readability.
SRCREV = "1bbcdd89fe81a7eaf132eb91bfcfac4961d91a59"
PV = "0.2+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libmodbus"
RDEPENDS:${PN} = "libmodbus"

# Upstream Makefile uses bare $(CC) and expects modbus.h via -lmodbus's
# pkg-config include dir (/usr/include/modbus). Pass that include path
# explicitly since the Makefile does not call pkg-config itself.
CFLAGS:append = " -I${STAGING_INCDIR}/modbus"

do_compile() {
    oe_runmake
}

PMOD_RS485_INSTALL_DIR = "/opt/xilinx/pmod-rs485-test"

do_install() {
    oe_runmake install DESTDIR=${D} prefix=${PMOD_RS485_INSTALL_DIR}
}

FILES:${PN} = "${PMOD_RS485_INSTALL_DIR}"
