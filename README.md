# ES-2Sem-2021-Grupo-13

ISCTE - LEI-PL Grupo 13

David Sebastião	- N.º60671

José Galo - N.º77897

Maria Sousa - N.º87435

Omelyan Shchomak - N.º72789

Salomé Salvado - N.º73354


Este repositório contém o projeto de extração das métricas de código e de detecção de code smells como god_class e long_method.
Aplicação permite fazer o parsing de qualquer projeto de java; extrair as métricas como número de métodos das classes, 
nº de linhas de código das classes e  dos métodos; calcular complexidade ciclomática e as estatisticas gerais do projeto.
É possivel calcular code smells tais como long_method e god_class. Utilizador pode criar e alterar as suas regras de detecção de code smells
que são guardadas num ficheiro xml. As regras são escritas em JavaScript e são interpretadas pelo interpretador interno de graalVM. 
Utilizador pode usar algumas variáveis já pre definidas que podem ser usadas na definição de regras.


COMO EXECUTAR?
Pode ser descarregado o jar executável. É importante ter em conta que o ficheiro xml com regras deve estar na mesma diretoria onde está 
a ser executado o jar. Também é preciso ter um projeto de teste "jasml" na diretoria onde está a ser executado o jar. Este projeto pode
ser descarregado e serve de boneco de teste contra o qual vai se testar acurácia das regras de code smells definidas pelos utilizadores.
Outro ficheiro que é preciso ter na diretoria de execução  é o ficheiro de excel chamado "code_smells.xlsx" que indica a detecção de code smells
de referência para o teste das regras dos utilizadores. Aplicação compara os code smells detetados pela regra do utilizador com os
code smells definidos no excel.

FUNCIONAMENTO DAS REGRAS DE DETECÇÃO DE CODE SMELLS
Utilizador pode usar as seguinter variáveis de input:
    - NOM_class
    - LOC_class
    - WMC_class
    - LOC_method
    - CYCLO_method
 
 Variáveis de output:
     - god_class
     - long_method
  
 Usando estas variáveis de input, é possível  escrever as regras como no seguinte exemplo: "if(LOC_method>10)long_method=true; if(LOC_class>10)god_class=true;"
 interpretador antes de fazer detecção de code smells vai vai inicializar estas variáveis com métricas calculadas a partir de um dados projeto. Após o processamento
 das regras serão devolvidos flags a indicar se foram detetados code smells como long_method e god_class.
 
 
 GERAÇÃO E IMPORTAÇÃO DO FICHEIRO EXCEL
 
 Aplicação permite criar o ficheiro excel com as métricas cálculadas após o processamento de um programa java. Gera excel com 11 colunas com dados em string.
 Para importar de forma bem sucedida o excel é preciso respeitar o numero de colunas que a aplicação gera e importa.
 
 
 





