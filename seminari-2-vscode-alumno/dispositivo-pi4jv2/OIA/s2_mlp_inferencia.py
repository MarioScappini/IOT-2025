import numpy as np
from sklearn import datasets 
import matplotlib.pyplot as plt
import time  # Importamos el módulo time

# Cargar dataset de dígitos
digits = datasets.load_digits()
image  = digits.images[4]
print(image)

# Mostrar la imagen
plt.imshow(image, cmap=plt.cm.gray_r, interpolation="nearest")

# Número de neuronas de las capas, incluida entrada y salida
nn = [64, 128, 512, 1024, 512, 128, 10]

# Preparar datos para la capa de entrada (8 x 8 = 64)
xtest = image.reshape(64)
print(xtest)

# Generar datos para las capas ocultas del MLP
W = []
b = []
for i in range(len(nn)-1):
    Wtmp = np.random.rand(nn[i+1],nn[i])
    W.append(Wtmp)
    
    btmp = np.random.rand(nn[i+1])
    b.append(btmp)
    
# Por curiosidad, comprobamos tamaños de las capas ocultas
for i in range(len(nn)-1):
    print("Capa oculta {}: (m, n) = {}".format(i+1, W[i].shape))

# Función no-lineal ReLU
def relu(x):
    return x * (x > 0)

# Temporizar la inferencia
start_time = time.time()

# Paso 2: Inferencia
a = xtest
for i in range(len(nn)-1):
    layer_start = time.time()  # Tiempo de inicio de la capa
    z = W[i] @ a + b[i]
    a = relu(z)
    layer_end = time.time()  # Tiempo de fin de la capa
    print(f"Tiempo en la capa {i+1}: {layer_end - layer_start:.6f} segundos")

ypred = a

end_time = time.time()
print(f"Tiempo total de inferencia: {end_time - start_time:.6f} segundos")
