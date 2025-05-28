import numpy as np
import time
import gemm

# Repeticiones
nreps = 20

# Datos
m = 1000
n = 1000
k = 1000
flops = 2.0 * m * n * k

rng = np.random.default_rng()
A = rng.random((m, k), dtype=np.float32)
B = rng.random((k, n), dtype=np.float32)

MC =  128
NC =  128
KC =  128

# Experimentos
tini = time.time()
for r in range(nreps):
    C = np.zeros([m, n], dtype=np.float32)
    gemm.gemm(A, B, C, MC, NC, KC)
tfin = (time.time() - tini) / nreps
gflops = flops / (tfin * 1.0e+9)

residual = np.linalg.norm(C - A @ B) / np.linalg.norm(C)
print("Tiempo = {:2.2}, GFLOPS = {:2.2}, Error relativo = {:2.2}".format(tfin, gflops, residual))
