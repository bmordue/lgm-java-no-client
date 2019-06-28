sed -i~ "s/image: lgm:.*/image: lgm:$(git rev-parse --short HEAD)/g" src/main/docker/app-stack.yml
