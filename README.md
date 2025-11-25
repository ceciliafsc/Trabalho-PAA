# Sistema de Otimização - PAA

Este projeto resolve problemas de Corte de Peças e Distribuição de Carga.

## Estrutura do Projeto

O projeto segue a organização padrão do repositório:

- `Main.java`: Ponto de entrada da aplicação.
- `model/`: Classes de modelo (`Peca`, `Placa`).
- `algoritmos_parte1/`: Algoritmos de Corte (Heurística, B&B, Força Bruta).
- `algoritmos_parte2/`: Algoritmos de Distribuição (Heurística, B&B, Força Bruta).
- `ver/`: Interface Gráfica e Visualização.
- `util/`: Utilitários de leitura de arquivo.
- `entradas/`: Arquivos de teste (.txt).

## Como Executar

### VSCode (Recomendado)
1. Abra o arquivo `Main.java`.
2. Clique no botão **Run** (Executar).

### Terminal
```bash
# Compilar
javac -d bin Main.java

# Executar
java -cp bin Main
```

## Funcionalidades
- **Corte**: Heurística, Branch & Bound e Força Bruta.
- **Distribuição**: Heurística, Branch & Bound e Força Bruta.
- **Visualização**: Interface gráfica completa.
