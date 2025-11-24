# Projeto PAA - Corte de Estoque e Particionamento

## Descrição
Implementação de algoritmos para otimização de corte de estoque e particionamento de conjunto.

## Estrutura
- `Main.java`: Ponto de entrada principal
- `Benchmark.java`: Script de análise de performance
- `model/`: Classes de modelo (Peca, Placa)
- `algoritmos_parte1/`: Algoritmos de corte (ForcaBruta, BranchAndBound, Heuristica)
- `algoritmos_parte2/`: Algoritmos de particionamento (ForaBruta, BnB2)
- `ver/`: Interface gráfica (Visualizador)

## Como Executar

### Compilar
```bash
javac -d bin -cp . Main.java model/*.java algoritmos_parte1/*.java algoritmos_parte2/*.java ver/*.java
```

### Executar Aplicação Principal
```bash
java -cp bin Main
```

### Executar Benchmark
```bash
javac -d bin -cp . Benchmark.java model/*.java algoritmos_parte1/*.java algoritmos_parte2/*.java
java -cp bin Benchmark
```

## Arquivos de Entrada
- `entrada_pequena.txt`: 3 peças
- `entrada.txt`: 7 peças (padrão)
- `entrada_media.txt`: 10 peças
- `entrada_grande.txt`: 15 peças

Formato:
```
<número de peças>
<altura1> <largura1>
<altura2> <largura2>
...
```

## Resultados
Ver `relatorio_tecnico.md` para análise completa de performance e comparação de algoritmos.

## Autores
Projeto desenvolvido para a disciplina de PAA - PUC Minas
