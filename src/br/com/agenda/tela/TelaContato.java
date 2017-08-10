/**
 * SIMPLES AGENDA DE CONTATOS (CRUD)
 *
 * @ AUTHOR THIAGO LUIZ SALES
 */
package br.com.agenda.tela;

/*
IMPORTAÇÃO DE PACOTES E RECURSOS
 */
import br.com.agenda.dal.Dal;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 * CLASSE PRINCIPAL QUE HERDA CARACTERISTICAS (CODIGOS)DO JFRAME EXTENDS ->
 * HERANÇA
 */
public class TelaContato extends javax.swing.JFrame {
// INICIANDO VARIAVEIS E OBJETOS PARA TRABALHAR COM O BANCO DE DADOS

    Connection conexao = null;// CONEXAO COM O BANCO
    PreparedStatement pst = null;// EXECUTAR COMANDOS SQL
    ResultSet rs = null;// RETORNO(BANCO DE DADOS)

    /**
     * MÉTODO CONSTRUTOR
     */
    public TelaContato() {
        initComponents();
        //INICIAR MODULO DE CONEXAO DAL
        conexao = Dal.conector();
        //System.out.println(conexao);// apoio ao entendimento do problemas
        // A ESTRUTURA ABAIX TROCA O ICONE DE ACORDO COM O ESTATUS DE CONEXAO COM O BANCO DE DADOS
        //(STRING - CONECTADO, NULL - DESCONECTADO)
        if (conexao != null) {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_database_connect_35950.png")));
        } else {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_database_error_35953.png")));
        }

    }
    // CRUD -> CREATE(INSERT) - READ(SELECT) - UPDATE(UPDATE) - DELETE(DELETE)
    // MÉTODOS
    // METODO PARA CRIAR OS CAMPOS

    // METODO LIMPAR
    private void limpar() {
        txtFone.setText("");
        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        lblId.setForeground(Color.BLACK);
        lblNome.setForeground(Color.black);
        lblFone.setForeground(Color.BLACK);
    }

    //METODO CONSULTAR(READ)
    private void consultar() {
        // CRIAR A QUERY
        // IREMOS SUBSTITUIR ? COM O CONTEUDO DA CAIXA txtID
        String consulta = "select * from tb_contatos where id=?";
        // USAREMOS UMA ESTRUTURA DO TIPO TRY CATCH PARA TRATAMENTO DE EXCEÇÕES
        try {
            //LOGICA PRINCIPAL
            pst = conexao.prepareStatement(consulta);
            // A LINHA ABAIXO CAPTURA O CONTEUDO DA CAIXA  DE TEXTO txtID e substitui PELO PARAMETRO ?
            pst.setString(1, txtId.getText());
            // A LINHA ABAIXO EXECUTA A QUERY E RECUPERA OS DADOS DO BANCO
            rs = pst.executeQuery();
            // SE EXISTIR  UM REGISTRO CONTATO CORRESPONDENTE AO ID, PREENCHER AS CAIXAS DE TEXTO
            if (rs.next()) {
                txtNome.setText(rs.getString(2));// PREENCHER O CAMPO txtNome FORMULARIO COM O CONTEUDO DO CAMPO 2 DA TABELA DE CONTATOS
                txtFone.setText(rs.getString(3));
                txtEmail.setText(rs.getString(4));
            } else {
                JOptionPane.showMessageDialog(null, "DADOS NÃO CADASTRADOS");
                limpar();
            }
        } catch (Exception e) {
            // CASO OCORRA UMA EXCEÇÃO, EXIBIR A MENSAGEM DE ERRO
            JOptionPane.showMessageDialog(null, e);
            // USANDO AS VARIAVEIS CONEXAO, PST, RS PARA PESQUISAR E RECUPERAR O CONTEUDO DO BANCO DE DADOS
        }

    }

    // METODO ADICIONAR(CREATE)
    private void adicionar() {
        String inserir = "insert into tb_contatos(id,nome,fone,email) values (?,?,?,?)";
        try {
            //LOGICA PRINCIPAL
            pst = conexao.prepareStatement(inserir);
            // PASSAGEM DE PARAMETRO ????
            pst.setString(1, txtId.getText());
            pst.setString(2, txtNome.getText());
            pst.setString(3, txtFone.getText());
            pst.setString(4, txtEmail.getText());
            // LOGICA USADA PARA VALIDAÇÃO DOS CAMPOS
            if ((txtId.getText().isEmpty()) || (txtNome.getText().isEmpty()) || (txtFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS");
                lblId.setForeground(Color.red);
                lblNome.setForeground(Color.red);
                lblFone.setForeground(Color.red);
            } else {
                // A LOGICA ABAIXO É USADA PARA IDENTIFICAR QUE A TABELA ATUALIZOU
                // CONFIRMA É UMA VARIAVEL DO  TIPO INTEIRO QUE RECEBE UM VALOR 1 SE FOI INSERIDO UMA LINHA NO BANCO DE DADOS
                int confirma = pst.executeUpdate();
                // A LINHA ABAIXO É USADA PARA ENETENDIMENTO DA LÓGICA
                //System.out.println(confirma);
                if (confirma == 1) {
                    JOptionPane.showMessageDialog(null, "CADASTRADO COM SUCESSO!");
                    limpar();
                }
            }
        } catch (Exception e) {
            // MENSAGEM AMIGAVEL APÓS VALIDAÇÃO DOS CAMPOS
            JOptionPane.showMessageDialog(null,e);

        }
    }

    // METODO ALTERAR(UPDATE)
    private void alterar() {
        String atualizar = "update tb_contatos set nome=?,fone=?,email=? where id=?";
        try {
            //LOGICA PRINCIPAL
            pst = conexao.prepareStatement(atualizar);
            // PASSAGEM DE PARAMETRO ????
            pst.setString(1, txtNome.getText());// ATENÇÃO !!! NESSE CASO O PRIMEIRO PARAMETRO É O NOME
            pst.setString(2, txtFone.getText());                
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtId.getText());// NESSE CASO O IDE É O ULTIMO PARAMETRO
            if ((txtId.getText().isEmpty()) || (txtNome.getText().isEmpty()) || (txtFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS");
                lblId.setForeground(Color.red);
                lblNome.setForeground(Color.red);
                lblFone.setForeground(Color.red);
            } else {
               int confirma = pst.executeUpdate(); 
               if (confirma == 1) {
                    JOptionPane.showMessageDialog(null, "CADASTRO ATUALIZADO COM SUCESSO!");
                    limpar();
                } 
            }
        } catch (Exception e) {
            // MENSAGEM AMIGAVEL APÓS VALIDAÇÃO DOS CAMPOS
            JOptionPane.showMessageDialog(null, "O CAMPO ID NÃO PERMITE VALORES DUPLICADOS");

        }
    }
    
    // METODO APAGAR(DELETE)
    private void apagar(){
        String deletar = "delete from tb_contatos where id=?";
        try {
            pst=conexao.prepareStatement(deletar);
            pst.setString(1, txtId.getText());
            int excluir = JOptionPane.showConfirmDialog(null, "TEM CERTEZA QUE DESEJA APAGAR ESTE CONTATO?","ATENÇÃO",JOptionPane.YES_NO_OPTION);
            if (excluir==JOptionPane.YES_OPTION) {
               int confirma = pst.executeUpdate(); 
               if (confirma == 1) {
                    JOptionPane.showMessageDialog(null, "CADASTRO REMOVIDO COM SUCESSO!");
                    limpar();
               }     
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblFone = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnRead = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        txtFone = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Agenda de Contatos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setResizable(false);

        lblId.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblId.setText("ID");

        lblNome.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNome.setText("NOME");

        lblFone.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFone.setText("FONE");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("EMAIL");

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_user-add_383199.png"))); // NOI18N
        btnCreate.setToolTipText("INSERIR INFORMAÇÕES");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_logs_2429331.png"))); // NOI18N
        btnRead.setToolTipText("PESQUISAR CADASTRO");
        btnRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_meanicons_24_197210.png"))); // NOI18N
        btnDelete.setToolTipText("EXCLUIR CADASTRO");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_edit_2460290.png"))); // NOI18N
        btnUpdate.setToolTipText("ALTERAR INFORMAÇÕES");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/agenda/icones/if_database_error_35953.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(lblFone)
                            .addComponent(lblNome)
                            .addComponent(lblId))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNome)
                            .addComponent(txtFone, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addComponent(lblStatus))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRead, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(lblId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNome))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(lblStatus))
                .addGap(18, 18, 18)
                .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnRead, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(205, 205, 205))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtEmail, txtFone, txtNome});

        setSize(new java.awt.Dimension(318, 479));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void btnReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadActionPerformed
        consultar();
    }//GEN-LAST:event_btnReadActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        apagar();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        adicionar();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
      alterar();
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaContato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaContato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaContato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaContato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaContato().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblFone;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFone;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
