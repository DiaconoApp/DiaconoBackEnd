package com.diacono.diacono.usecases;

/**
 * Contrato base para use cases com entrada e saída simples.
 * Use cases com múltiplos parâmetros podem sobrescrever execute com assinaturas específicas.
 */
public interface UseCase<I, O> {
    O execute(I input);
}
