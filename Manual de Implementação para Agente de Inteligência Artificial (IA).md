# Manual de Implementação para Agente de Inteligência Artificial (IA)

Este manual detalha o processo de implementação do projeto de PAA (Plano de Ação e Arquitetura) para o "Trabalho de Implementação" da PUC Minas, que aborda problemas de otimização de corte de estoque e particionamento de conjunto.

**Linguagem de Implementação:** Python 3.x

## 1. Configuração do Ambiente e Estrutura de Código

### 1.1. Configuração Inicial

O agente de IA deve garantir que as bibliotecas necessárias para visualização e manipulação de dados estejam instaladas.

**Ação da IA:**
1.  Executar o comando de instalação:
    ```bash
    pip3 install matplotlib numpy
    ```

### 1.2. Estrutura de Arquivos

O código deve ser organizado conforme a arquitetura definida no PAA:

| Arquivo | Descrição |
| :--- | :--- |
| `main.py` | Ponto de entrada, orquestração e execução de testes. |
| `gui.py` | Funções de leitura de arquivo e visualização gráfica. |
| `parte1_corte_estoque.py` | Modelagem e algoritmos para o problema de corte. |
| `parte2_transporte.py` | Modelagem e algoritmos para o problema de transporte. |
| `entrada_corte.txt` | Arquivo de exemplo para a Parte 1. |
| `entrada_transporte.txt` | Arquivo de exemplo para a Parte 2. |

## 2. Modelagem e Funções Auxiliares

### 2.1. Constantes e Modelagem (Parte 1)

As seguintes constantes devem ser definidas em `parte1_corte_estoque.py`:

```python
# Constantes do Problema de Corte de Estoque
PLACA_LARGURA = 300
PLACA_ALTURA = 300
CUSTO_PLACA = 1000.00
CUSTO_CORTE_CM = 0.01
MARGEM_MINIMA = 10
```

**Função de Custo Total:**
A função de custo total deve ser implementada para calcular o custo final de uma solução (conjunto de placas utilizadas e cortes realizados).

$$
Custo_{total} = (N_{placas} \times 1000.00) + (L_{corte} \times 0.01)
$$

### 2.2. Função `cabe_e_custo(peca, placa)`

Esta é a função central da Parte 1. Ela deve determinar se uma peça retangular pode ser colocada em uma posição $(x, y)$ de uma placa, respeitando a margem mínima de 10cm, e calcular o comprimento de corte adicional necessário.

**Pseudocódigo (Lógica de Encaixe Simplificada - First Fit):**

```pseudocode
FUNÇÃO cabe_e_custo(peca_largura, peca_altura, placa_ocupacao)
  // placa_ocupacao é uma matriz ou lista de retângulos já colocados
  
  PARA cada PONTO (x, y) na PLACA
    // 1. Verificar Margem Mínima (10cm)
    SE x < MARGEM_MINIMA OU y < MARGEM_MINIMA ENTÃO
      CONTINUAR
    FIM SE
    
    // 2. Verificar Limites da Placa
    SE (x + peca_largura + MARGEM_MINIMA) > PLACA_LARGURA OU (y + peca_altura + MARGEM_MINIMA) > PLACA_ALTURA ENTÃO
      CONTINUAR
    FIM SE
    
    // 3. Verificar Colisão com Peças Existentes
    COLISAO = FALSO
    PARA cada RETANGULO_EXISTENTE em placa_ocupacao
      SE peca_em_posicao(x, y, peca_largura, peca_altura) INTERSECTA RETANGULO_EXISTENTE ENTÃO
        COLISAO = VERDADEIRO
        SAIR
      FIM SE
    FIM PARA
    
    SE COLISAO É FALSO ENTÃO
      // Encaixe encontrado. Calcular custo de corte.
      // O custo de corte é o perímetro da peça (2 * (largura + altura)) se for um novo corte.
      // Em implementações mais complexas, deve-se considerar se o corte é compartilhado.
      // Para simplificar, considere o perímetro da peça como o custo adicional de corte.
      CUSTO_CORTE_ADICIONAL = 2 * (peca_largura + peca_altura) * CUSTO_CORTE_CM
      RETORNAR VERDADEIRO, x, y, CUSTO_CORTE_ADICIONAL
    FIM SE
  FIM PARA
  
  RETORNAR FALSO, 0, 0, 0
FIM FUNÇÃO
```

## 3. Algoritmos de Otimização (Parte 1: Corte de Estoque)

### 3.1. Força-Bruta (Brute Force)

O algoritmo deve gerar todas as permutações de peças e, para cada permutação, tentar encaixar as peças sequencialmente, minimizando o custo total.

**Pseudocódigo:**

```pseudocode
FUNÇÃO forca_bruta_corte(pecas)
  MELHOR_CUSTO = INFINITO
  MELHOR_POSICIONAMENTO = NULO
  
  PARA cada PERMUTACAO de pecas
    CUSTO_ATUAL = 0
    PLACAS_ATUAIS = [NOVA_PLACA]
    
    PARA cada PECA em PERMUTACAO
      ENCAIXADA = FALSO
      PARA cada PLACA em PLACAS_ATUAIS
        SE cabe_e_custo(PECA, PLACA) RETORNA VERDADEIRO ENTÃO
          // Atualizar PLACA com a nova peça e CUSTO_ATUAL
          ENCAIXADA = VERDADEIRO
          SAIR
        FIM SE
      FIM PARA
      
      SE ENCAIXADA É FALSO ENTÃO
        // Abrir nova placa
        NOVA_PLACA = NOVA_PLACA
        // Tentar encaixar PECA na NOVA_PLACA
        // Atualizar CUSTO_ATUAL com CUSTO_PLACA e custo de corte
        ADICIONAR NOVA_PLACA a PLACAS_ATUAIS
      FIM SE
    FIM PARA
    
    SE CUSTO_ATUAL < MELHOR_CUSTO ENTÃO
      MELHOR_CUSTO = CUSTO_ATUAL
      MELHOR_POSICIONAMENTO = PLACAS_ATUAIS
    FIM SE
  FIM PARA
  
  RETORNAR MELHOR_CUSTO, MELHOR_POSICIONAMENTO
FIM FUNÇÃO
```

### 3.2. Branch-and-Bound (B&B)

Utiliza a estrutura de busca da Força-Bruta, mas com poda.

**Pseudocódigo:**

```pseudocode
FUNÇÃO branch_and_bound_corte(pecas_restantes, placas_atuais, custo_atual, melhor_custo_global)
  SE pecas_restantes ESTÁ VAZIA ENTÃO
    RETORNAR custo_atual
  FIM SE
  
  // Cálculo do Lower Bound (Limite Inferior)
  // Custo mínimo estimado para as peças restantes.
  // Exemplo: Custo_Min_Restante = (Custo_Placa * Num_Min_Placas_Necessarias) + Custo_Corte_Minimo
  LOWER_BOUND = custo_atual + calcular_lower_bound(pecas_restantes)
  
  SE LOWER_BOUND >= melhor_custo_global ENTÃO
    // Poda: Este ramo não levará a uma solução melhor
    RETORNAR INFINITO
  FIM SE
  
  PECA_ATUAL = PRIMEIRO elemento de pecas_restantes
  MELHOR_CUSTO_RAMO = INFINITO
  
  // Tentar encaixar PECA_ATUAL em placas existentes ou em uma nova
  
  // 1. Tentar em placas existentes
  PARA cada PLACA em placas_atuais
    SE cabe_e_custo(PECA_ATUAL, PLACA) RETORNA VERDADEIRO ENTÃO
      // Criar novo estado (placas_novas, custo_novo)
      CUSTO_RAMO = branch_and_bound_corte(pecas_restantes - PECA_ATUAL, placas_novas, custo_novo, melhor_custo_global)
      MELHOR_CUSTO_RAMO = MIN(MELHOR_CUSTO_RAMO, CUSTO_RAMO)
      melhor_custo_global = MIN(melhor_custo_global, MELHOR_CUSTO_RAMO)
    FIM SE
  FIM PARA
  
  // 2. Tentar em uma nova placa
  // Criar novo estado (placas_novas_nova_placa, custo_novo_nova_placa)
  CUSTO_RAMO = branch_and_bound_corte(pecas_restantes - PECA_ATUAL, placas_novas_nova_placa, custo_novo_nova_placa, melhor_custo_global)
  MELHOR_CUSTO_RAMO = MIN(MELHOR_CUSTO_RAMO, CUSTO_RAMO)
  
  RETORNAR MELHOR_CUSTO_RAMO
FIM FUNÇÃO
```

### 3.3. Heurística (First Fit Decreasing - FFD)

**Pseudocódigo:**

```pseudocode
FUNÇÃO heuristica_corte(pecas)
  ORDENAR pecas por ÁREA (ou dimensão maior) em ordem DECRESCENTE
  PLACAS_ATUAIS = [NOVA_PLACA]
  CUSTO_ATUAL = CUSTO_PLACA
  
  PARA cada PECA em pecas
    ENCAIXADA = FALSO
    PARA cada PLACA em PLACAS_ATUAIS
      SE cabe_e_custo(PECA, PLACA) RETORNA VERDADEIRO ENTÃO
        // Atualizar PLACA com a nova peça e CUSTO_ATUAL
        ENCAIXADA = VERDADEIRO
        SAIR
      FIM SE
    FIM PARA
    
    SE ENCAIXADA É FALSO ENTÃO
      // Abrir nova placa
      NOVA_PLACA = NOVA_PLACA
      // Tentar encaixar PECA na NOVA_PLACA
      // Atualizar CUSTO_ATUAL com CUSTO_PLACA e custo de corte
      ADICIONAR NOVA_PLACA a PLACAS_ATUAIS
    FIM SE
  FIM PARA
  
  RETORNAR CUSTO_ATUAL, PLACAS_ATUAIS
FIM FUNÇÃO
```

## 4. Algoritmos de Otimização (Parte 2: Particionamento de Conjunto)

### 4.1. Força-Bruta (Brute Force)

O algoritmo deve gerar todas as combinações possíveis para o Grupo 1.

**Pseudocódigo:**

```pseudocode
FUNÇÃO forca_bruta_transporte(pesos)
  MELHOR_DIFERENCA = INFINITO
  MELHOR_PARTICAO = NULO
  
  // Gerar todas as 2^N partições (combinações para o Grupo 1)
  PARA cada SUBSET em CONJUNTO_DE_TODOS_SUBSETS(pesos)
    GRUPO1 = SUBSET
    GRUPO2 = pesos - SUBSET
    
    SOMA1 = SOMA(GRUPO1)
    SOMA2 = SOMA(GRUPO2)
    DIFERENCA_ATUAL = ABS(SOMA1 - SOMA2)
    
    SE DIFERENCA_ATUAL < MELHOR_DIFERENCA ENTÃO
      MELHOR_DIFERENCA = DIFERENCA_ATUAL
      MELHOR_PARTICAO = (GRUPO1, GRUPO2)
    FIM SE
  FIM PARA
  
  RETORNAR MELHOR_DIFERENCA, MELHOR_PARTICAO
FIM FUNÇÃO
```

### 4.2. Branch-and-Bound (B&B)

Utiliza a estrutura de busca em árvore para alocar cada peça a um dos dois grupos.

**Pseudocódigo:**

```pseudocode
FUNÇÃO branch_and_bound_transporte(pecas_restantes, soma1, soma2, melhor_diferenca_global)
  SE pecas_restantes ESTÁ VAZIA ENTÃO
    RETORNAR ABS(soma1 - soma2)
  FIM SE
  
  // Cálculo do Lower Bound (Limite Inferior)
  // A diferença mínima possível é a diferença atual menos a soma de todas as peças restantes.
  // Lower_Bound = Max(0, Diferenca_Atual - Soma_Pesos_Restantes)
  DIFERENCA_ATUAL = ABS(soma1 - soma2)
  SOMA_RESTANTE = SOMA(pecas_restantes)
  LOWER_BOUND = MAX(0, DIFERENCA_ATUAL - SOMA_RESTANTE)
  
  SE LOWER_BOUND >= melhor_diferenca_global ENTÃO
    // Poda: Este ramo não levará a uma solução melhor
    RETORNAR INFINITO
  FIM SE
  
  PECA_ATUAL = PRIMEIRO elemento de pecas_restantes
  PECAS_PROXIMO = pecas_restantes - PECA_ATUAL
  
  // 1. Alocar PECA_ATUAL ao Grupo 1
  DIFERENCA_RAMO1 = branch_and_bound_transporte(PECAS_PROXIMO, soma1 + PECA_ATUAL.peso, soma2, melhor_diferenca_global)
  melhor_diferenca_global = MIN(melhor_diferenca_global, DIFERENCA_RAMO1)
  
  // 2. Alocar PECA_ATUAL ao Grupo 2
  DIFERENCA_RAMO2 = branch_and_bound_transporte(PECAS_PROXIMO, soma1, soma2 + PECA_ATUAL.peso, melhor_diferenca_global)
  melhor_diferenca_global = MIN(melhor_diferenca_global, DIFERENCA_RAMO2)
  
  RETORNAR MIN(DIFERENCA_RAMO1, DIFERENCA_RAMO2)
FIM FUNÇÃO
```

### 4.3. Heurística (Largest Processing Time - LPT Adaptado)

**Pseudocódigo:**

```pseudocode
FUNÇÃO heuristica_transporte(pesos)
  ORDENAR pesos em ordem DECRESCENTE
  GRUPO1 = []
  GRUPO2 = []
  SOMA1 = 0
  SOMA2 = 0
  
  PARA cada PESO em pesos
    SE SOMA1 <= SOMA2 ENTÃO
      ADICIONAR PESO a GRUPO1
      SOMA1 = SOMA1 + PESO
    SENÃO
      ADICIONAR PESO a GRUPO2
      SOMA2 = SOMA2 + PESO
    FIM SE
  FIM PARA
  
  DIFERENCA = ABS(SOMA1 - SOMA2)
  RETORNAR DIFERENCA, (GRUPO1, GRUPO2)
FIM FUNÇÃO
```

## 5. Instruções para Testes e Coleta de Dados

### 5.1. Medição de Tempo

O agente de IA deve usar a biblioteca `time` do Python para medir o tempo de execução de cada algoritmo.

```python
import time

def medir_tempo(funcao, *args):
    inicio = time.time()
    resultado = funcao(*args)
    fim = time.time()
    tempo_execucao = fim - inicio
    return resultado, tempo_execucao
```

### 5.2. Casos de Teste

O agente de IA deve criar e executar testes com pelo menos 3 tamanhos de entrada (pequeno, médio, grande) para cada parte.

**Exemplo de Tabela de Resultados (A ser preenchida pela IA):**

| Parte | Algoritmo | Tamanho da Entrada (N peças) | Custo/Diferença Final | Tempo de Execução (s) |
| :---: | :--- | :---: | :---: | :---: |
| 1 | Força-Bruta | 5 | R$ X.XX | Y.YYY |
| 1 | B&B | 10 | R$ X.XX | Y.YYY |
| 1 | Heurística | 50 | R$ X.XX | Y.YYY |
| 2 | Força-Bruta | 10 | Z | W.WWW |
| 2 | B&B | 20 | Z | W.WWW |
| 2 | Heurística | 100 | Z | W.WWW |

## 6. Geração do Relatório Técnico Final

O agente de IA deve compilar todos os resultados e a documentação em um Relatório Técnico final, seguindo a estrutura do PAA e os requisitos do documento original.

### 6.1. Estrutura do Relatório (Arquivo `relatorio_tecnico.md`)

1.  **Introdução:** Descrever o problema (Corte de Estoque e Particionamento) e o objetivo (Implementação e Comparação de Algoritmos).
2.  **Solução Proposta:** Incluir os pseudocódigos detalhados (Seção 3 e 4 deste manual).
3.  **Implementação:** Detalhar a escolha do Python, o uso do Matplotlib para a GUI e as otimizações de código (ex: como o Lower Bound foi calculado no B&B).
4.  **Relatório de Testes:**
    *   Apresentar a tabela de resultados (Seção 5.2).
    *   Incluir as imagens geradas pela função `visualizar_corte` para os exemplos da Parte 1.
5.  **Conclusão:**
    *   Discutir a complexidade teórica ($O(n!)$ para Força-Bruta, $O(2^n)$ para Particionamento Força-Bruta).
    *   Comparar os tempos de execução medidos (tabela) e a qualidade das soluções (custo/diferença).
    *   Discutir a eficácia da poda no B&B e a qualidade da solução da heurística.
6.  **Bibliografia:** Seguir o padrão ABNT (se necessário, a IA deve buscar exemplos de citação ABNT para artigos de algoritmos).

Este manual fornece o roteiro completo para a implementação. O agente de IA deve seguir estes passos para codificar, testar e documentar o projeto.
