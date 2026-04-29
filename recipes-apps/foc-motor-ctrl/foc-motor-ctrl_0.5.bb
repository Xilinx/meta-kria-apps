# SPDX-License-Identifier: MIT
# Copyright (C) 2026 Advanced Micro Devices, Inc.

SUMMARY = "Field Oriented Control (FOC) motor-control library, Python bindings, CANopen server and Bokeh dashboard"
DESCRIPTION = "Builds the foc-motor-ctrl library (libfoc_motor_ctrl), the \
pybind11 Python bindings (py_foc_motor_ctrl), the CANopen-driven motor \
server (fmc_canopen / start_motor_server), and the Bokeh-based dashboard \
launcher used on Kria KD240."
HOMEPAGE = "https://github.com/Xilinx/foc-motor-ctrl"
SECTION = "apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=370aacba9d9e1a362c89a2b36dc0608d"

SRC_URI = "git://github.com/Xilinx/foc-motor-ctrl.git;branch=main;protocol=https"
SRCREV = "3573b84076383c7f59472bb11b56af15fb1d38bd"

S = "${WORKDIR}/git"

inherit cmake python3targetconfig pkgconfig

# - libiio: target lib for the IIO motor-control sysfs interfaces
# - python3-pybind11: headers/cmake config for the python binding
# - lely-core: provides liblely-coapp + headers for the CANopen server
DEPENDS = " \
    pkgconfig-native \
    libiio \
    python3 \
    python3-pybind11 \
    lely-core \
"

# Build the standalone CANopen motor-control server and the Bokeh dashboard
# launcher; the cmdline app and tests are not shipped. Let Yocto handle
# post-install symlink creation rather than the upstream CMake snippet.
EXTRA_OECMAKE = " \
    -DBUILD_CANOPEN_APP=ON \
    -DBUILD_DASHBOARD_APP=ON \
    -DBUILD_CMDLINE_APP=OFF \
    -DBUILD_TEST=OFF \
    -DSKIP_POST_INSTALL=ON \
    -DPYBIND11_FINDPYTHON=ON \
    -DPython3_INCLUDE_DIR=${STAGING_INCDIR}/python${PYTHON_BASEVERSION} \
    -DPython3_LIBRARY=${STAGING_LIBDIR}/libpython${PYTHON_BASEVERSION}${PYTHON_ABI}.so \
    -DPython_INCLUDE_DIR=${STAGING_INCDIR}/python${PYTHON_BASEVERSION} \
    -DPython_LIBRARY=${STAGING_LIBDIR}/libpython${PYTHON_BASEVERSION}${PYTHON_ABI}.so \
"

do_install:append() {
    # Recreate the /etc/motor-control/config -> ${prefix}/etc/motor-control/anaheim.config
    # symlink that upstream's post-install snippet would have made.
    install -d ${D}${sysconfdir}/motor-control
    ln -sf ${prefix}/etc/motor-control/anaheim.config \
        ${D}${sysconfdir}/motor-control/config
}

RDEPENDS:${PN} = " \
    bash \
    libiio \
    python3 \
    liblely-coapp2 \
    python3-bokeh \
    python3-numpy \
"

# pybind11 strips the python module .so during build, which trips Yocto's
# already-stripped QA check; suppress it for this single package.
INSANE_SKIP:${PN} += "already-stripped"

# CMake exported targets file contains absolute paths from the build dir
# under TMPDIR. They are harmless on the rootfs (only consumed by other
# CMake projects targeting the SDK) but trip the buildpaths QA gate.
INSANE_SKIP:${PN}-dev += "buildpaths"

FILES:${PN} += " \
    ${libdir}/libfoc_motor_ctrl.so* \
    ${libdir}/py_foc_motor_ctrl*.so \
    ${bindir}/fmc_canopen \
    ${bindir}/start_motor_server \
    ${bindir}/start_motor_dashboard \
    ${prefix}/etc/motor-control \
    ${sysconfdir}/motor-control \
    ${datadir}/foc-motor-ctrl \
"
