# DefaultListableBeanFactory 공부

- 혼자 공부한 부분이라 주관적이므로 부족하거나 틀린 부분이 있다면 알려주세요 

> DefaultListableBeanFactory 는 ApplicationContext 에서 빈을 관리하는 부분에 해당한다.
> 빈을 관리한다는 것은 크게 CRUD 의 기능으로 이해하면 좋을 것 같다.

> DefaultListableBeanFacoty 의 상속구조<br>
> DefaultListableBeanFacotry -> AbstractAutowireCapableBeanFactory -> AbstractBeanFactory -> FactoryBeanRegistrySupport -> DefaultSingletonBeanFactory -> SimpleAliasRegistry<br>

> SimpleAliasRegistry : 빈의 별칭을 관리하는 클래스<br>
> DefaultSingletonBeanRegistry : 싱글톤 인스턴스를 관리하는 클래스<br>
> FactoryBeanRegistrySupport : FactoryBean 으로 구현된 Bean 들을 관리하는 클래스 (참고로 FactoryBean 은 일반적인 케이스로 구현하기 어려운 빈들을 말한다. ex) Dynamic Proxy)<br>
> AbstractBeanFactory : 빈 팩토리의 골격 클래스(많은 일을 해서 구체적으로 모르겠다. 아직 정리하기가 어렵다. 추후 보완) <br>
> AbstractAutowireCapableBeanFactory : 빈을 생성하는 메소드와 이를 도와주는 메소드를 구현한 추상 구현 클래스(빈 생성/빈 주입 등을 담당한다)<br>
> DefaultListableBeanFactory : BeanDefinition 을 기반으로 빈을 관리하는 자체로 사용 가능한 클래스<br>

## 빈은 크게 2가지 방법으로 등록 가능하다.

1. 싱글톤으로 등록 (registerSingleton)
    - 싱글톤은 인스턴스로 등록한다.
    - 인스턴스를 DefaultSingletonBeanRegistry 의 singletonObjects Map 에 추가한다.  

2. BeanDefinition 을 생성하여 등록 (registerBeanDefinition)
    - 보통 클래스파일을 BeanDefinition 에 저장하여 등록한다.
    - 유효성 검사를 통과한 BeanDefinition 은 DefaultListableBeanFactory 의 beanDefinitionMap Map 에 추가한다.

3. 이외에도 여러가지(dependent bean, alias 등)를 등록할 수 있는데 추후에 점점 추가할 예정이다.

## 빈을 가져오는 방법 (getBean -> doGetBean)

1. 싱글톤 오브젝트에 등록되있는 경우 (getSingleton)
    - 앞서 말한 DefaultSingletonRegistry 의 singletonObjects Map 에서조회 하여 가져온다.

2. BeanDefinition 에 등록되있는 경우
    - 싱글톤으로 등록되어 있는 경우 1번의 케이스를 따른다.
    - 싱글톤으로 등록되지 않았을 경우 다음의 순서를 따른다.
        - AbstractBeanFactory 의 mergedBeanDefinition Map 에 없는 경우 복사하여 스코프 설정(싱글톤) 뒤 추가한뒤 반환한다.. (AbstractBeanFactory - getMergedLocalBeanDefinition)
        - 반환받은 BeanDefinition 으로 유효성 검사를 진행한다.(AbstractBeanFactory - checkMergedBeanDefinition)
        - mergedBeanDefinition 으로 객체를 생성한다. (AbstractAutowireCapableBeanFactory - createBean -> doCreateBean -> createBeanInstance -> instantiateBean)
            - instantiateBean 메소드 안에서 getInstantiateionStrategy() 메소드로 빈을 생성할 전략을 가져와 instantiate() 메소드를 호출 해 빈을 생성한다.
            - 예) SimpleInstantiationStrategy(BeanDefinition 에 저장된 Class 로 기본 생성자를 추출한 뒤 newInstance() 로 인스턴스를 생성)
        - 초기화를 진행한다. (AbstractAutowireCapableBeanFactory - initializeBean)
            - Aware 류를 구현했을 경우 세팅해준다. (빈이름, BeanClassLoader, BeanFactory 등)                     
            - BeanFactory 에 등록된 BeanPostProcessor(AbstractBeanFactory 의 BeanPostProcessors Map) 가 있다면 beanPostProcessor 의 <b>postProcessBeforeInitialization</b> 메소드를 실행시킨다.
            - Bean 이 InitializingBean 를 구현했다면 afterPropertiesSet 메소드를 실행시킨다.
            - BeanFactory 에 등록된 BeanPostProcessor(AbstractBeanFactory 의 BeanPostProcessors Map) 가 있다면 beanPostProcessor 의 <b>postProcessAfterInitialization</b> 메소드를 실행시킨다.
        - 객체를 만들고 DefaultSingletonBeanRegistry 의 singletonObjects Map 에 추가한다.
    - 만약 부모 빈 팩토리가 존재하고 자신의 빈 팩토리에 BeanDefinition 이 없으면 부모의 빈팩토리에서 검색해 가져온다.
    
3. 빈을 가져올 때 처리되는 많은 부분이 생략되었는데 추후에 점점 추가할 예정이다.
    