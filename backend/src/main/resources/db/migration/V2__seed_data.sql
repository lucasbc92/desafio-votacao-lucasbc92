-- Inserindo pautas de exemplo
INSERT INTO pauta (titulo, descricao, data_criacao, ativo) VALUES
('Assembleia Geral Extraordinária - Reforma do Estatuto',
 'Discussão e votação sobre as mudanças propostas no estatuto social da cooperativa, incluindo atualização das cláusulas de participação e distribuição de sobras.',
 CURRENT_TIMESTAMP, TRUE),

('Aprovação de Novos Investimentos em Tecnologia',
 'Votação sobre a proposta de investimento em modernização dos sistemas digitais da cooperativa para melhorar a experiência dos associados.',
 CURRENT_TIMESTAMP, TRUE),

('Política de Sustentabilidade Ambiental',
 'Proposta de implementação de práticas sustentáveis nas operações da cooperativa, incluindo redução de plástico e incentivo ao uso racional de recursos.',
 CURRENT_TIMESTAMP, TRUE),

('Distribuição de Sobras do Exercício 2024',
 'Deliberação sobre o percentual de distribuição das sobras apuradas no exercício financeiro de 2024 entre os associados.',
 CURRENT_TIMESTAMP, TRUE),

('Criação de Fundo de Reserva para Emergências',
 'Proposta de constituição de fundo de reserva equivalente a 10% das sobras anuais para cobertura de eventuais prejuízos futuros.',
 CURRENT_TIMESTAMP, TRUE);

-- Inserindo sessões de votação (algumas abertas, outras fechadas)
INSERT INTO sessao_votacao (pauta_id, data_abertura, data_fechamento, duracao_segundos, status) VALUES
(1, CURRENT_TIMESTAMP, NULL, 300, 'ABERTA'),
(2, CURRENT_TIMESTAMP, NULL, 180, 'ABERTA'),
(3, TIMESTAMPADD(MINUTE, -10, CURRENT_TIMESTAMP), TIMESTAMPADD(MINUTE, -5, CURRENT_TIMESTAMP), 300, 'FECHADA'),
(4, TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP), 600, 'FECHADA'),
(5, NULL, NULL, 120, 'PENDENTE');

-- Inserindo votos para sessões fechadas (usando pauta_id)
INSERT INTO voto (pauta_id, cpf_associado, voto_tipo, data_voto) VALUES
(3, '12345678901', 'SIM', TIMESTAMPADD(MINUTE, -8, CURRENT_TIMESTAMP)),
(3, '23456789012', 'NAO', TIMESTAMPADD(MINUTE, -7, CURRENT_TIMESTAMP)),
(3, '34567890123', 'SIM', TIMESTAMPADD(MINUTE, -6, CURRENT_TIMESTAMP)),
(3, '45678901234', 'SIM', TIMESTAMPADD(MINUTE, -5, CURRENT_TIMESTAMP)),
(3, '56789012345', 'NAO', TIMESTAMPADD(MINUTE, -5, CURRENT_TIMESTAMP)),

(4, '12345678901', 'SIM', TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP)),
(4, '23456789012', 'SIM', TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP)),
(4, '34567890123', 'NAO', TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP)),
(4, '45678901234', 'SIM', TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP)),
(4, '56789012345', 'SIM', TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP)),
(4, '67890123456', 'NAO', TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP)),
(4, '78901234567', 'SIM', TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP));