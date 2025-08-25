# Changelog

## [0.1.0](https://github.com/NERDHEAD-lab/json-schema/compare/0.1.0-SNAPSHOT...0.1.0) (2025-08-25)


### Features

* **CI/CD:** build 과정 간소화를 위해 jpackage plugin 수정 시도 (description 참고) ([517da81](https://github.com/NERDHEAD-lab/json-schema/commit/517da8122c554122fa738a1f97ffa3c79d720366))
* **CI/CD:** packager에서 jpackage-maven-plugin을 통해 실행가능한 cli 모듈로 배포 구성 ([6c91d26](https://github.com/NERDHEAD-lab/json-schema/commit/6c91d261132eb545a16d4b59f9d76cd750cda7fd))
* DTO에 json schema를 정의하기 위한 SchemaDefinition 추가 ([c3a5a8f](https://github.com/NERDHEAD-lab/json-schema/commit/c3a5a8fc3a4e594128514f3ddf7a77f4fe3108dc))
* SchemaDefinition의 기본 값 변경 및 SchemaProperty의 최신화 ( DRAFT_05 ~ DRAFT_2020_12 ) ([08f7edb](https://github.com/NERDHEAD-lab/json-schema/commit/08f7edb1e529555c463a7bf8bacccd11bd682652))
* spring-shell-starter 보안 취약점 조치 ([7c61539](https://github.com/NERDHEAD-lab/json-schema/commit/7c6153940f0f2551104541f43bf029fa4f08061a))
* 검증관련 기능을 JsonSchemaGenerator에서 분리 ( JsonSchemaGenerator -&gt; JsonSchemaValidator ) ([5063d51](https://github.com/NERDHEAD-lab/json-schema/commit/5063d511f971b213a5802a784e55eebdc32f9ceb))
* 다른 프로젝트에서 테스트용으로 구현되었던 JsonSchemaGenerator 추가 ([2fbe660](https://github.com/NERDHEAD-lab/json-schema/commit/2fbe6604f3b51cccde1375d4a5ac56cda0237358))
* 불용할것으로 예상되는 jpackage-maven-plugin 제거 ([f18a61a](https://github.com/NERDHEAD-lab/json-schema/commit/f18a61a776a94b700a136d443366ba25f143e63a))
* 테스트 및 개발 참고용 test entity 추가 ( @SchemaDefinition 적용 ) ([d331398](https://github.com/NERDHEAD-lab/json-schema/commit/d331398786cfbce1804502f1f7e76a831b5fce45))


### Bug Fixes

* repository url 수정 ([4fc3d19](https://github.com/NERDHEAD-lab/json-schema/commit/4fc3d19fb9caa464abdd4f71ad5af857a2424cbf))


### Code Refactoring

* **CI/CD:** refact release-please action ([bcd1e9d](https://github.com/NERDHEAD-lab/json-schema/commit/bcd1e9db6ca85409b9a95b3923059026f8777e53))


### Chores

* init release-please action ([e836045](https://github.com/NERDHEAD-lab/json-schema/commit/e8360455dd534895ffeeafa43808bc62d1e565c4))
