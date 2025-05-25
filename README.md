# Sistema de Alocação de Salas em uma Instituição de Ensino

## Descrição

Este sistema visa gerenciar a alocação de salas para turmas em uma instituição de ensino. O objetivo é evitar conflitos de uso de salas, garantir a disponibilidade de infraestrutura adequada e organizar horários de aulas. O sistema deve permitir o cadastro de salas, turmas, professores, disciplinas e horários.

Cada sala possui uma capacidade e recursos (como projetor, lousa digital etc.). As turmas são associadas a disciplinas e professores, e devem ser alocadas em horários específicos e salas disponíveis. O sistema deve evitar conflitos como: dois eventos na mesma sala e horário, ou um professor atribuído a duas turmas simultaneamente.

Relatórios de uso de sala, disponibilidade por horário e distribuição de turmas por sala devem ser oferecidos, com exportação em PDF ou CSV.

## Requisitos Funcionais

### 1. Gerenciamento de Salas

- **REQ01**: Permitir o gerenciamento de salas, incluindo nome, localização, capacidade máxima e recursos disponíveis.
- **REQ02**: Permitir a marcação de salas como indisponíveis em períodos específicos (ex: manutenção).

### 2. Gerenciamento de Professores e Disciplinas

- **REQ03**: Permitir o gerenciamento de professores, incluindo nome, CPF, e-mail e disciplinas que leciona.
- **REQ04**: Permitir o gerenciamento de disciplinas com nome, carga horária semanal e descrição.

### 3. Gerenciamento de Turmas

- **REQ05**: Permitir o gerenciamento de turmas, incluindo identificação única, semestre, turno e lista de alunos.
- **REQ06**: Cada turma deve estar vinculada a uma disciplina e a um professor responsável.

### 4. Definição de Horários

- **REQ07**: Permitir o cadastro de blocos de horário (ex: 08h–10h, 10h–12h) e dias da semana.
- **REQ08**: Garantir que uma turma seja alocada apenas em horários disponíveis e compatíveis com a carga horária da disciplina.
- **REQ09**: Impedir que um professor ou sala sejam alocados em mais de uma turma no mesmo horário.

### 5. Alocação de Salas

- **REQ10**: Permitir a alocação de salas a turmas para horários específicos, considerando capacidade mínima e recursos necessários.
- **REQ11**: Validar a disponibilidade da sala antes de confirmar a alocação.
- **REQ12**: Permitir alteração da alocação de uma turma, desde que não haja conflito com outra turma no mesmo horário.

### 6. Consultas e Verificações

- **REQ13**: Permitir consultar a grade semanal de cada sala, com horários ocupados e livres.
- **REQ14**: Permitir verificar o horário semanal de cada professor e de cada turma.
- **REQ15**: Informar conflitos de horário automaticamente ao tentar salvar alocações inconsistentes.

### 7. Relatórios e Estatísticas

- **REQ16**: Gerar relatórios de uso de salas por período, com taxa de ocupação e horários mais utilizados.
- **REQ17**: Gerar relatório de distribuição de turmas por turno, professor ou disciplina.
- **REQ18**: Permitir a exportação dos relatórios em **PDF** e **CSV**, com colunas organizadas, agrupamentos e totais.

### 8. Regras e Restrições

- **REQ19**: A sala atribuída a uma turma deve ter capacidade igual ou superior ao número de alunos da turma.
- **REQ20**: Uma sala marcada como indisponível não pode ser atribuída a nenhuma turma no período informado.
- **REQ21**: Professores não podem estar alocados a mais de uma turma no mesmo bloco de horário.

## Possíveis APIs/Bibliotecas a Serem Usadas

- **JavaFX** – Interface gráfica do sistema.
- **JDBC / Hibernate** – Persistência de dados e controle de relacionamentos.
- **iText / JasperReports** – Exportação de relatórios em PDF.
- **Apache POI** – Geração de planilhas CSV ou Excel com grade horária ou uso de salas.
- **JUnit / Mockito** – Testes de lógica de conflito de alocação.
- **Java Time API** – Manipulação de datas, horários e intervalos.

## Integrantes do grupo com nome completo
* João Pedro de Araújo Souza - joao.pedroaraujosouza@ufrpe.br 
* Mario Jorge - mario.jorgeandrade@ufrpe.br 
* Luiz Maranhão - luiz.maranhao@ufrpe.br
* Pedro Roma - pedro.roma@ufrpe.br  
