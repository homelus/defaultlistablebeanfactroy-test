# DefaultListableBeanFactory Study (Spring core 3.0.0 기반)

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

## <p>빈을 등록하는 프로세스</p>

1. 싱글톤으로 등록 (registerSingleton)
    - 싱글톤은 인스턴스로 등록한다.
    - 인스턴스를 DefaultSingletonBeanRegistry 의 singletonObjects Map 에 추가한다.  

2. BeanDefinition 을 생성하여 등록 (registerBeanDefinition)
    - 보통 클래스파일을 BeanDefinition 에 저장하여 등록한다.
    - 유효성 검사를 통과한 BeanDefinition 은 DefaultListableBeanFactory 의 beanDefinitionMap Map 에 추가한다.

3. 이외에도 여러가지(dependent bean, alias 등)를 등록할 수 있는데 추후에 점점 추가할 예정이다.

## <p>빈을 가져오는 프로세스</p> (AbstractBeanFactory : getBean -> doGetBean)

1. 싱글톤 오브젝트에 등록되있는 경우 (getSingleton)
    - 앞서 말한 DefaultSingletonRegistry 의 singletonObjects Map 에서조회 하여 가져온다.
    - 가져온 빈이 팩토리 빈일 경우 팩토리 빈의 시그니쳐인 getObject 의 메소드를 호출하여 객체를 가져온다. (싱글톤인 경우 팩토리 빈에서 가져온 객체를 캐시해 놓는다.)

2. BeanDefinition 에 등록되있는 경우
    - 싱글톤으로 등록되어 있는 경우 1번의 케이스를 따른다.
    - 싱글톤으로 등록되지 않았을 경우 다음의 순서를 따른다.
        - 만약 부모 빈 팩토리가 존재하고 자신의 빈 팩토리에 BeanDefinition 이 없으면 부모의 빈팩토리에서 검색 후 반환한다.
        - 부모 빈팩토리에 빈이 없다면 다음의 순서를 따른다.
            - AbstractBeanFactory 의 mergedBeanDefinition Map 에 없는 경우 복사하여 스코프 설정(싱글톤) 뒤 추가한뒤 반환한다.. (AbstractBeanFactory - getMergedLocalBeanDefinition)
            - BeanDefinition 으로 객체를 생성한다. (기존에 존재하는 BeanDefinition 이 있다면 merge 하여 사용한다.) - (AbstractBeanFactory - getMergedLocalBeanDefinition)
            - 생성한 BeanDefinition 으로 유효성 검사를 진행한다.(AbstractBeanFactory - checkMergedBeanDefinition)
            - 먼저 설정되야 하는 빈(dependsOn)이 있다면 해당 빈들을 생성한다. (추후 내용 추가)
            - Bean 의 scope 에 따라 아래 3가지로 나뉜다.
                1. Singleton(DefaultSingletonBeanRegistry - getSingleton)
                   - 빈을 생성하는 프로세스를 실행한 후 생성된 객체를 반환한다.
                   - 가져온 빈이 팩토리 빈일 경우 팩토리 빈의 시그니쳐인 getObject 의 메소드를 호출하여 객체를 가져온다. (싱글톤인 경우 팩토리 빈에서 가져온 객체를 캐시해 놓는다.)
                2. Prototype
                3. Etc(Request, Session 등등..) 
            

## <p>빈을 생성하는 프로세스</p> (AbstractAutowireCapableBeanFactory: createBean -> doCreateBean)
1. resolveBeanClass - Class 파일에 대한 유효성 검사같은 느낌(추후 보강 필요)
2. BeanDefinition: prepareMethodOverrides - BeanDefinition 에 정의된 OverrideMethod 의 유효성 검사와 준비 시키는 일(잘 모르겠음 보강 필요)
3. resolveBeforeInstantiation - BPP 에게 프록시를 대신 만들어 줄 기회를 제공한다.
    - BPP 목록에 InstantiationAwareBeanPostProcessor 가 존재할 경우 postProcessBeforeInstantiation 를 실행시켜 빈을 만든다.
4. <b>doCreateBean<b> - 빈을 생성한다.
    1. createBeanInstance - BeanWrapper 를 생성한다.
        1. resolveBeanClass - Class 파일 유효성을 검사한다.
        2. 상황에 따라 빈을 만드는 방법이 있다. 그중 기본적인 부분만 먼저 설명 (FactoryMethod, 생성자(파라미터를 가진 생성자), 기본(기본 생성자) 3개가 있음. 추후 보강 필요)
        3. <b>instantiateBean<b> - 기본 생성자를 가지고 빈을 생성한다.
            1. getInstantiationStrategy - instantiate
                1. 빈을 생성할 전략(InstantiationStrategy)을 가져온다. (기본 전략은 CglibSubclassingInstantiationStrategy 이다.)
                2. <b>instantiate<b> - 빈을 생성한다.
                    - CglibSubclassingInstantiationStrategy 의 상위 클래스 SimpleInstantiationStrategy 의 instantiate 메소드를 호출한다.
                        - (예외 CglibSubclassingInstantiationStrategy 에는 하위 클래스가 있고 이를 통해 Cglib 에서제공하는 Enhancer 를 통해 프록시 객체를 만들 수 있다. 추후 보강 필요) 
                    - <b>BeanUtils.instantiateClass<b> 메소드를 생성할 빈의 Constructor 파라미터와 함께 호출한다.
                        - Constructor 의 <b>newInstance<b> 메소드를 통해 객체를 생성한다.
    2. applyMergedBeanDefinitionPostProcessors - 필요에 따라 BeanDefinition 을 수정한다. (잘 모르겠음 보강 필요)
    3. addSingletonFactory 필요에 따라 실행 (잘 모르겠음 보강 필요)
    4. populateBean - 객체의 Property 에 필요한 정보를 주입한다.
    5. initializeBean - 빈의 초기화를 실행한다.
         - Aware 류를 구현했을 경우 세팅해준다. (빈이름, BeanClassLoader, BeanFactory 등)                     
        - BeanFactory 에 등록된 BeanPostProcessor(AbstractBeanFactory 의 BeanPostProcessors Map) 가 있다면 beanPostProcessor 의 <b>postProcessBeforeInitialization</b> 메소드를 실행시킨다.
        - Bean 이 InitializingBean 를 구현했다면 afterPropertiesSet 메소드를 실행시킨다.
        - BeanFactory 에 등록된 BeanPostProcessor(AbstractBeanFactory 의 BeanPostProcessors Map) 가 있다면 beanPostProcessor 의 <b>postProcessAfterInitialization</b> 메소드를 실행시킨다.
    5. registerDisposableBeanIfNecessary - 잘모르겠음. (보강 필요)
5. 반환한다.
    
※ 빈을 가져올 때 처리되는 많은 부분이 생략되었는데 추후에 점점 추가할 예정이다.
    
