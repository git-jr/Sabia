![banner-gemini-android](https://github.com/user-attachments/assets/a889bb11-8733-459f-9737-93a3dcdd2759)


# Sabiá
Um aplicativo Android um aplicativo criado para ajudar pessoas no aprendizado de idiomas e que agora receberá funções de IA para tornar a experiência do usuário mais interativa.

## 🔨 Funcionalidades do projeto

https://github.com/user-attachments/assets/18634249-5aea-4cfe-b174-7ec06b916e96


### 📱 Telas
- **Seleção de tema:** Permite selecionar um tema de texto ou enviar uma imagem / foto para ser analisada pelo Gemini e gerar um tema com base no conteúdo dela. Assim como o idioma que quer praticar.
- **Complete a frase:** Apresenta uma frase incompleta e pede para o usuário completá-la com uma palavra ou expressão. Tudo gerado e corrigido com auxilio do Gemin.
- **Envio de imagem:** O Gemini solicita no idioma que quer ser praticado, uma imagem / foto de acordo com o tema para ser analisado e verificar se o usuário entendeu soube o que era ou não.
- **Finalização:** Após todos lições serem completadas, o usuário recebe uma mensagem de parabéns e é exibido quantos dias consecutivos ele já praticou.

## ✔️ Técnicas e tecnologias utilizadas


As técnicas e tecnologias utilizadas pra isso são:

- `Jetpack Compose`: kit de ferramentas moderno para criar IUs em dispositivos móveis.
- `Kotlin`: linguagem de programação.
- `Gradle Version Catalogs`: nova forma de gerenciar plugins e dependências em projetos Android.
- `Material Design 3`: padrão de design recomendado pela Google para criação de UI modernas.
- `Navigating with Compose`: navegação entre composables e telas.
- `Viewmodel, states e flow`: gerenciamento de estados e controle dos eventos disparados pelas detecções do modelo da Google.
- `CameraX`: biblioteca do Jetpack que facilita a integração de funcionalidades de câmera em aplicativos Android, abstraindo a complexidade da API de câmera do Android e oferecendo uma interface simples para captura de fotos e vídeo.
- `Camera Permissions`: gerencia o acesso à câmera do dispositivo, solicitando permissão ao usuário para utilizá-la nas detecções e interações dentro do aplicativo.
- `Photo Picker`: ferramenta que facilita a seleção de imagens diretamente da galeria do dispositivo Android.
- `Hilt-Dagger`: framework de injeção de dependências que simplifica a configuração e gerenciamento de dependências no Android.
- `Secrets Gradle Plugin`: gerencia de forma segura a chave de API do projeto, evitando que informações sensíveis sejam expostas no código-fonte.
- `Google Gemini`: modelo de IA que permite processar e interpretar textos e imagens, gerando respostas contextuais e interagindo com o usuário de forma inteligente.
- `Google AI SDK`: SDK oficial do Google para integrar modelos de IA no Android, permitindo chamadas eficientes à API do Gemini.


## 📁 Acesso ao projeto

- Versão inicial: Veja o [código fonte][codigo-inicial] ou [baixe o projeto][download-inicial]
- Versão final: Veja o [código fonte][codigo-final] ou [baixe o projeto][download-final]

## 🛠️ Abrir e rodar o projeto

Após baixar o projeto, você pode abri-lo com o Android Studio. Para isso, na tela de launcher clique em:

1. **"Open"** (ou alguma opção similar).
2. Procure o local onde o projeto está e o selecione (caso o projeto seja baixado via zip, é necessário extraí-lo antes de procurá-lo).
3. Por fim, clique em **"OK"**.

O Android Studio executará algumas *tasks* do Gradle para configurar o projeto. Aguarde até finalizar. Após isso, você pode executar o App 🏆

### Configurando chaves de API com o Secrets Gradle Plugin

Este projeto utiliza o **Secrets Gradle Plugin** para gerenciar de forma segura as chaves de API. Antes de executar o aplicativo, é necessário adicionar a chave do Google Gemini ao arquivo `local.properties` no seguinte formato:

```properties
apiKey=sua-chave-aqui
```

Caso você ainda não tenha uma chave, acesse o [painel do Google AI Studio](https://aistudio.google.com/app/apikey) para gerar uma nova chave de API.

Feito isso, você estará pronto para executar o app com todas as funcionalidades habilitadas! 🚀


## 📚 Mais informações do curso

Gostou do projeto e quer conhecer mais? Você pode [acessar a formação com esse e muitos outros cursos](https://www.alura.com.br/cursos-online-inteligencia-artificial/ia-para-mobile) relacioandos ao tema de Inteligência Artificial e Machine Learning no Android.

[codigo-inicial]: https://github.com/git-jr/4178-Gemini-Android-Imagens-Textos/commits/projeto-inicial
[download-inicial]: https://github.com/git-jr/4178-Gemini-Android-Imagens-Textos/archive/refs/heads/projeto-inicial.zip

[codigo-final]: https://github.com/git-jr/4178-Gemini-Android-Imagens-Textos/commits/aula-5/
[download-final]: https://github.com/git-jr/4178-Gemini-Android-Imagens-Textos/archive/refs/heads/aula-5.zip
