import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function UserDashboard() {
  const [entregas, setEntregas] = useState([]);
  const [statusMap, setStatusMap] = useState({});
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  // Obtém o userId do localStorage
  const userId = localStorage.getItem("userId");

  // Função para carregar as entregas do usuário
  useEffect(() => {
    const fetchEntregas = async () => {
      try {
        const response = await axios.get(`https://gerenciamento-de-entregas.vercel.app/entregas/usuario/${userId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        setEntregas(response.data);

        // Inicializa os estados dos dropdowns com o status atual das entregas
        const initialStatusMap = {};
        response.data.forEach((entrega) => {
          initialStatusMap[entrega.id] = entrega.status;
        });
        setStatusMap(initialStatusMap);
      } catch (error) {
        setErrorMessage("Erro ao carregar as entregas.");
        console.error("Erro ao buscar entregas", error);
      }
    };

    if (userId) {
      fetchEntregas();
    } else {
      setErrorMessage("Usuário não autenticado.");
    }
  }, [userId]);

  // Função para atualizar o status da entrega
  const handleStatusChange = async (entregaId) => {
    try {
      const entregaDTO = {
        usuarioID: userId,
        status: statusMap[entregaId], // Envia o status selecionado
      };

      const response = await axios.put(
        `https://gerenciamento-de-entregas.vercel.app/entregas/${entregaId}/atualizar-status`,
        entregaDTO,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      // Atualiza a lista de entregas com o novo status
      setEntregas((prevEntregas) =>
        prevEntregas.map((entrega) =>
          entrega.id === entregaId ? { ...entrega, status: response.data.status } : entrega
        )
      );
    } catch (error) {
      setErrorMessage("Erro ao atualizar o status da entrega.");
      console.error("Erro ao atualizar status", error);
    }
  };

  // Função para voltar à página de login
  const handleLogout = () => {
    localStorage.clear(); // Remove token e informações do usuário
    navigate("/login"); // Redireciona para a página de login
  };

  return (
    <div className="p-8">
      {/* Botão de Logout */}
      <div className="flex justify-end">
        <button
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded mb-4"
        >
          Sair
        </button>
      </div>

      <h1 className="text-3xl font-bold text-blue-600 mb-6">Área do Usuário</h1>

      {errorMessage && <div className="text-red-500 mb-4">{errorMessage}</div>}

      <div className="bg-white p-6 shadow-lg rounded-lg">
        <h2 className="text-2xl font-semibold mb-4 text-gray-700">Minhas Entregas</h2>

        {entregas.length === 0 ? (
          <p className="text-gray-600">Você não tem entregas no momento.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {entregas.map((entrega) => (
              <div key={entrega.id} className="bg-gray-100 p-4 rounded-lg shadow-md">
                <h3 className="font-semibold text-lg">{entrega.descricao}</h3>
                <p className="text-sm text-gray-600">
                  Status Atual: <span className="font-bold">{entrega.status}</span>
                </p>

                {/* Dropdown para selecionar o status */}
                <select
                  value={statusMap[entrega.id] || entrega.status}
                  onChange={(e) =>
                    setStatusMap((prev) => ({
                      ...prev,
                      [entrega.id]: e.target.value,
                    }))
                  }
                  className="mt-2 p-2 border rounded w-full"
                >
                  <option value="ENTREGUE">Entregue</option>
                </select>

                {/* Botão para atualizar o status */}
                <button
                  onClick={() => handleStatusChange(entrega.id)}
                  className="mt-3 w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded"
                >
                  Atualizar Status
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default UserDashboard;
