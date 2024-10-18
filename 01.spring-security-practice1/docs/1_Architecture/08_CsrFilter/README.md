# CsrfFilter
CsrfFilter는 CsrfAttack을 방어하는 Filter이다.  

<br>

## Csrf Attack이란?
![Filter](../../../images/08.Filter%207.PNG)

<br>

## Csrf Attack을 막기 위해서...
![Filter](../../../images/09.Filter%208.PNG)

<br>

CsrfFilter는 Csrf Token을 사용하여 위조된 페이지의 악의적인 공격을 방어한다.  
정상적인 페이지는 Csrf Token이 있을 것이고 위조된 페이지는 Csrf Token이 없거나 잘못된 Csrf Token을 가지고 있을 것이다.

따라서 정상적인 페이지에는 Csrf Token 값을 알려줘야 하는데 Thymeleaf에서는 페이지를 만들때 자동으로 Csrf Token을 넣어준다.  
따로 추가하지 않았는데 아래와 같은 코드가 form tag안에 자동으로 생성된다.  
대신 굳이 사용자에게 보여줄 필요가 없는 값이기 때문에 hidden으로 처리된다.  
```html
<input type="hidden" name="_csrf" value="594af42a-63e9-4ef9-aeb2-3687f12cdf43">
```
Csrf Filter는 자동으로 활성화되어 있는 Filter지만 명시적으로 On 하기 위해서는 ```http.csrf();``` 코드를 추가하면 된다.  
Off하기 위해서는 ```http.csrf().disable();```로 설정 하면 된다.