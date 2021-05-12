<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</head>
<body>
<div class="demo-layout mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="demo-header mdl-layout__header mdl-color--blue-800 mdl-color-text--white">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title">Home</span>
            <div class="mdl-layout-spacer"></div>
        </div>
    </header>
    <main class="mdl-layout__content mdl-color--grey-100">
        <div class="mdl-grid">
            <table class="mdl-data-table mdl-js-data-table mdl-color--white mdl-shadow--2dp mdl-cell mdl-cell--12-col">
                <thread>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric">User ID</th>
                        <th class="mdl-data-table__cell--non-numeric">Name</th>
                        <th class="mdl-data-table__cell--non-numeric">Email</th>
                        <th class="mdl-data-table__cell--non-numeric">Role</th>
                        <th class="mdl-data-table__cell--non-numeric">Assign New Role</th>
                    </tr>
                </thread>
                <tbody>
                <#list users as user>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">${user.id}</td>
                        <td class="mdl-data-table__cell--non-numeric">${user.name}</td>
                        <td class="mdl-data-table__cell--non-numeric">${user.email}</td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <#if user.role??>
                                <#if user.role == "admin">
                                    Admin
                                <#elseif user.role == "mlp">
                                    Medical Laboratory Professional
                                <#elseif user.role == "physician">
                                    Physician
                                <#elseif user.role == "nurse">
                                    Nurse
                                </#if>
                            <#else>
                                Not Assigned
                            </#if>
                        </td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <#if user.role??>
                                <#if user.role != "admin">
                                    <form method="post" action="dashboard">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <select id="role" name="role">
                                            <option value="" selected="selected" disabled="disabled" hidden="hidden">
                                                Select a role
                                            </option>
                                            <option value="mlp">Medical Laboratory Professional</option>
                                            <option value="physician">Physician</option>
                                            <option value="nurse">Nurse</option>
                                        </select>

                                        <input type="submit" value="Assign">
                                    </form>
                                </#if>
                            <#else>
                                <form method="post" action="dashboard">
                                    <input type="hidden" name="userId" value="${user.id}">
                                    <select id="role" name="role">
                                        <option value="" selected="selected" disabled="disabled" hidden="hidden">
                                            Select a role
                                        </option>
                                        <option value="mlp">Medical Laboratory Professional</option>
                                        <option value="physician">Physician</option>
                                        <option value="nurse">Nurse</option>
                                    </select>

                                    <input type="submit" value="Assign">
                                </form>
                            </#if>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>