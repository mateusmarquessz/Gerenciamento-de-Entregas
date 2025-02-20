import React, { useEffect, useState } from "react";

function AdminDashboard() {
  const [entregas, setEntregas] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [descricao, setDescricao] = useState("");
  const [usuarioID, setUsuarioID] = useState("");
  const [editingEntrega, setEditingEntrega] = useState(null);
  const [novoStatus, setNovoStatus] = useState("");
  const [isUserDashboard, setIsUserDashboard] = useState(false);
  const [newAdminData, setNewAdminData] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  useEffect(() => {
    fetchEntregas();
    fetchUsuarios();
  }, []);

  const fetchEntregas = async () => {
    const response = await fetch("https://gerenciamento-de-entregas.onrender.com/entregas", {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    const data = await response.json();
    setEntregas(data);
  };

  const fetchUsuarios = async () => {
    const response = await fetch("https://gerenciamento-de-entregas.onrender.com/api/users", {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    const data = await response.json();
    setUsuarios(data);
  };

  const criarEntrega = async () => {
    await fetch("https://gerenciamento-de-entregas.onrender.com/entregas", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify({ descricao, usuarioID }),
    });
    setDescricao("");
    setUsuarioID("");
    fetchEntregas();
  };

  const editarEntrega = async (id, novaDescricao, novoStatus) => {
    await fetch(`https://gerenciamento-de-entregas.onrender.com/entregas/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify({ descricao: novaDescricao, status: novoStatus }),
    });
    setEditingEntrega(null);
    fetchEntregas();
  };

  const deletarEntrega = async (id) => {
    await fetch(`https://gerenciamento-de-entregas.onrender.com/entregas/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    fetchEntregas();
  };

  const deletarUsuario = async (id) => {
    await fetch(`https://gerenciamento-de-entregas.onrender.com/api/users/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    fetchUsuarios();
  };

  const criarUsuarioAdmin = async () => {
    const response = await fetch("https://gerenciamento-de-entregas.onrender.com/api/users/register-admin", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(newAdminData),
    });
    const data = await response.json();
    if (response.ok) {
      alert("Administrador criado com sucesso!");
      setNewAdminData({ nome: "", email: "", senha: "" });
      fetchUsuarios();
    } else {
      alert(data);
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };


  return (
    <div className="p-8 max-w-4xl mx-auto bg-gray-400 min-h-screen flex flex-col items-center rounded-lg shadow-lg">
      <button
        onClick={logout}
        className="bg-red-600 text-white px-4 py-2 rounded-lg shadow mb-4 hover:bg-red-700 transition self-start"
      >
        Sair
      </button>
      <h1 className="text-4xl font-bold text-red-600 text-center mb-6">Área do Administrador</h1>

      {/* Alternância entre o Dashboard de Entregas e de Usuários */}
      <button
        onClick={() => setIsUserDashboard(!isUserDashboard)}
        className="bg-gray-600 text-white px-4 py-2 rounded-lg shadow mb-6 hover:bg-gray-700 transition"
      >
        {isUserDashboard
          ? "Voltar para o Dashboard de Configuração de Entregas"
          : "Ir para o Dashboard de Configuração de Usuário"}
      </button>

      {isUserDashboard ? (
        /* Dashboard de Usuários */
        <div className="w-full bg-white p-6 rounded-lg shadow-md">
          {/* Formulário para criar um novo admin */}
          <div className="bg-gray-200 p-6 rounded-lg shadow-md mb-6">
            <h3 className="text-xl font-bold mb-4">Criar Novo Administrador</h3>
            <input
              type="text"
              placeholder="Nome"
              value={newAdminData.nome}
              onChange={(e) =>
                setNewAdminData({ ...newAdminData, nome: e.target.value })
              }
              className="border p-2 rounded-lg mb-4 w-full"
            />
            <input
              type="email"
              placeholder="Email"
              value={newAdminData.email}
              onChange={(e) =>
                setNewAdminData({ ...newAdminData, email: e.target.value })
              }
              className="border p-2 rounded-lg mb-4 w-full"
            />
            <input
              type="password"
              placeholder="Senha"
              value={newAdminData.senha}
              onChange={(e) =>
                setNewAdminData({ ...newAdminData, senha: e.target.value })
              }
              className="border p-2 rounded-lg mb-4 w-full"
            />
            <button
              onClick={criarUsuarioAdmin}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition w-full"
            >
              Criar Administrador
            </button>
          </div>

          {/* Lista de usuários */}
          <h2 className="text-2xl font-bold text-center mb-4">Usuários</h2>
          <ul>
            {usuarios.map((usuario) => (
              <li
                key={usuario.id}
                className="border-b py-4 flex justify-between items-center"
              >
                <span>{usuario.nome}</span>
                <button
                  onClick={() => deletarUsuario(usuario.id)}
                  className="text-red-600 hover:text-red-800 transition"
                >
                  Deletar Usuário
                </button>
              </li>
            ))}
          </ul>
        </div>
      ) : (
        /* Dashboard de Entregas */
        <div className="w-full bg-white p-6 rounded-lg shadow-md">
          <h1 className="block mb-2">Registro de Entregas</h1>
          <div className="flex flex-col sm:flex-row gap-4 w-full">
            <input
              type="text"
              placeholder="Descrição da entrega"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              className="border p-2 flex-1 rounded-lg shadow-sm"
            />
            <select
              value={usuarioID}
              onChange={(e) => setUsuarioID(e.target.value)}
              className="border p-2 flex-1 rounded-lg shadow-sm"
            >
              <option value="">Selecione um funcionário</option>
              {usuarios.map((usuario) => (
                <option key={usuario.id} value={usuario.id}>
                  {usuario.nome}
                </option>
              ))}
            </select>
            <button
              onClick={criarEntrega}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition"
            >
              Criar Entrega
            </button>
          </div>

          {/* Lista de entregas */}
          <ul className="mt-6 w-full bg-white p-6 rounded-lg shadow-md">
            {entregas.map((entrega) => (
              <li
                key={entrega.id}
                className="border-b py-4 flex flex-col sm:flex-row justify-between items-center"
              >
                {editingEntrega === entrega.id ? (
                  <div className="flex flex-col sm:flex-row gap-2 w-full">
                    <input
                      type="text"
                      defaultValue={entrega.descricao}
                      onChange={(e) => setDescricao(e.target.value)}
                      className="border p-2 flex-1 rounded-lg shadow-sm"
                      autoFocus
                    />
                    <select
                      defaultValue={entrega.status}
                      onChange={(e) => setNovoStatus(e.target.value)}
                      className="border p-2 rounded-lg shadow-sm"
                    >
                      <option value="PENDENTE">Pendente</option>
                      <option value="EM_TRANSITO">Em Trânsito</option>
                      <option value="ENTREGUE">Entregue</option>
                    </select>
                    <button
                      onClick={() => editarEntrega(entrega.id, descricao, novoStatus)}
                      className="bg-green-600 text-white px-4 py-2 rounded-lg shadow hover:bg-green-700 transition"
                    >
                      Salvar
                    </button>
                  </div>
                ) : (
                  <div className="flex flex-col sm:flex-row gap-4 w-full justify-between">
                    <span className="text-lg font-medium">{entrega.descricao}</span>
                    <span className="text-sm text-gray-600">{entrega.status}</span>
                    <span className="text-sm text-gray-600">
                      {/* Exibe o nome do usuário associado */}
                      {entrega.usuarioNome}
                    </span>
                    <div className="flex gap-4">
                      <button
                        onClick={() => setEditingEntrega(entrega.id)}
                        className="text-blue-600 hover:text-blue-800 transition"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => deletarEntrega(entrega.id)}
                        className="text-red-600 hover:text-red-800 transition"
                      >
                        Deletar
                      </button>
                    </div>
                  </div>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
