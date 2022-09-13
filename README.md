# Projeto de Programação de Sistemas 💻

![GitHub repo size](https://img.shields.io/github/repo-size/guivahl/programacao-de-sistemas?style=for-the-badge)
![GitHub language count](https://img.shields.io/github/languages/count/guivahl/programacao-de-sistemas?style=for-the-badge)
![GitHub forks](https://img.shields.io/github/forks/guivahl/programacao-de-sistemas?style=for-the-badge)

## O que é? 
Esse repositório consiste no projeto desenvolvido durante a dicisplina de Programação de Sjstemas, onde nosso trabalho é construir uma máquina virtual e um montador no qual irão receber códigos em assembly como input e seu output será a execução do código fonte fornecido.

## Como executar? 🤔
Vamo agora ao passo a passo para você colocar as mãos na massa nesse projeto! 🤩

### Pré requisitos ✅

- Java versão >17
- Git >2.3

### Clonando repositório ✍🏼

```bash
$ git clone https://github.com/guivahl/programacao-de-sistemas.git
```

### Executando 👨‍💻

- Navegue até a pasta onde você clonou o projeto

```bash
$ cd virtual-machine  
```

- Entre na pasta `src`, para compilarmos o projeto

```bash
$ cd src
$ javac vm/App.java -d ../
```

- depois de compilar o arquivo, vamos voltar a pasta raiz e executá-lo 

```bash
$ cd ..
$ java vm.App  
```
