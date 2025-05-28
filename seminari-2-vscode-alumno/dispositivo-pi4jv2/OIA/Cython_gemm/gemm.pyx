import numpy as np

def packAc(Ac, A):
    (mc, kc) = A.shape
    Ac[0:mc,0:kc] = A.copy()

def packBc(Bc, B):
    (kc, nc) = B.shape
    Bc[0:kc,0:nc] = B.copy()

def gemm(A, B, C, MC, NC, KC):
#
    k      = A.shape[1]
    (m, n) = C.shape
#
    Ac = np.zeros([MC, KC])
    Bc = np.zeros([KC, NC])
#
    for jc in range(0, n, NC):              # Loop L1
        nc = min(n-jc, NC)

        for pc in range(0, k, KC):               # L2
            kc = min(k-pc, KC)
            # Pack Bc (emulated as a simple copy)
            packBc(Bc, B[pc:pc+kc,jc:jc+nc])

            for ic in range(0, m, MC):           # L3
                mc = min(m-ic, MC)
                # Pack Ac (emulated as a simple copy)
                packAc(Ac, A[ic:ic+mc,pc:pc+kc])
        
                C[ic:ic+mc,jc:jc+nc] += Ac[0:mc,0:kc] @ Bc[0:kc,0:nc]
