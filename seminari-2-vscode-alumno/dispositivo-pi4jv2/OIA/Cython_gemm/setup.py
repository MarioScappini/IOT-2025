from setuptools import setup
from Cython.Build import cythonize

setup(ext_modules = cythonize("gemm.pyx"))
# setup(ext_modules = cythonize("relu.pyx"))
