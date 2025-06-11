<#macro loginForm>
    <form id="ajaxLoginForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div id="errorMessage" style="color: red; margin-bottom: 10px; display: none;"></div>
        <div class="form-element">
            <label for="username">Email:</label>
            <input type="text" id="username" name="username">
        </div>
        <div class="form-element">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password">
        </div>
        <div class="form-element">
            <button type="submit">Войти</button>
        </div>
    </form>
</#macro>


<#macro registerForm>
    <form id="registrationForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div id="errorMessage" style="color: red; margin-bottom: 10px; display: none;"></div>
        <div class="form-element">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username">
            <div id="usernameError" class="error" style="color: red;"></div>
        </div>
        <div class="form-element">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email">
            <div id="emailError" class="error" style="color: red;"></div>
        </div>
        <div class="form-element">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password">
            <div id="passwordError" class="error" style="color: red;"></div>
        </div>
        <div class="form-element">
            <label for="confirmPassword">Подтверждение пароля:</label>
            <input type="password" id="confirmPassword" name="confirmPassword">
            <div id="confirmPasswordError" class="error" style="color: red;"></div>
        </div>
        <div class="form-element">
            <button type="submit">Зарегистрироваться</button>
        </div>
    </form>
</#macro>