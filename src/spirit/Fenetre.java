/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit;

import Filtering.ComboBoxFilterDecorator;
import Filtering.CustomComboRenderer;
import controle.SmartControler;
import datamanager.outils.MPoste;
import datamanager.outils.Observateur;
import datamanager.outils.PManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import spirit.customisation.MyJListeCellRender;
import spirit.customisation.MyTableCellRender;
import spirit.entites.Client;
import spirit.entites.DetailVente;
import spirit.entites.Produit;
import spirit.entites.Vente;

/**
 *
 * @author genius
 */
public class Fenetre extends javax.swing.JFrame implements Observateur {

    private JProgressBar initProgress;
    private JPopupMenu jpm = new JPopupMenu();
    private JMenuItem pOne = new JMenuItem("+1 au panier");
    private JMenuItem pTwo = new JMenuItem("+2 au panier");
    private JMenuItem mOne = new JMenuItem("-1 au panier");
    private JMenuItem mTwo = new JMenuItem("-2 au panier");

    private SmartControler controler;
    private MPoste user = null;

    /**
     * Creates new form Fenetre
     */
    public Fenetre(JProgressBar progress) {
        initProgress = progress;
        initProgress.setMaximum(100);
        System.out.println("spirit.Fenetre.<init>()");
        initComponents();

        //Progress
        initProgress.setValue(5);

        initPopUp();

        //Progress
        initProgress.setValue(10);

        approPan.add(new PanneauApprovisionnement(), BorderLayout.CENTER);

        ((DefaultTableModel) panier.getModel()).setRowCount(0);

        //Progress
        initProgress.setValue(15);

        configure();
        configureTable();
        configureCombo();
        
        //Progress
        initProgress.setValue(95);

        retirer.setEnabled(false);

        vendre.setEnabled(false);

        manageCard();
        liste_prod.setCellRenderer(new MyJListeCellRender());
        
        try {
            Thread.sleep(5000);
            initProgress.setValue(100);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void configureTable() {

        accueil.setEnabled(false);
        //Configuration de l'entête du tableau des produits
        String[] headers = {"Code", "Designation", "Prix Unitaire", "Quantité en stock"};
        ((DefaultTableModel) produits.getModel()).setColumnIdentifiers(headers);

        produits.setRowHeight(30);
        clients.setRowHeight(30);
        ventes.setRowHeight(30);
        listeDetail.setRowHeight(30);
        panier.setRowHeight(30);

        clients.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                supprimerClient.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        produits.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Ligne selectionner : " + produits.getSelectedRow());;
                supprimer.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getClickCount() == 2) {
                    Produit selected = liste_prod.getSelectedValue();
                    DetailVente line = new DetailVente(0, selected.getPrix(), null, selected);
                    controler.addToPanier(1, line);
                }
            }
        };
        
        
        //Progress
        initProgress.setValue(40);

        liste_prod.addMouseListener(listener);

        panier.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                retirer.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        /**
         * Gestion de la modification du contenu d'une cellule quantité
         */
        panier.getColumn("Quantite").setCellRenderer(new MyTableCellRender());
        panier.getColumn("Prix unitaire").setCellRenderer(new MyTableCellRender());
        panier.getColumn("Total").setCellRenderer(new MyTableCellRender());
        //panier.getDefaultEditor(columnClass).getCellEditor().addCellEditorListener(new CellEditListener());
        panier.getDefaultEditor(panier.getColumnClass(2)).addCellEditorListener(new CellEditListener());
        
        //Progress
        initProgress.setValue(70);
    }

    public void configure() {
        System.out.println("spirit.Fenetre.configure()");

        controler = new SmartControler();
        controler.setObserteur(this);

        supprimer.setEnabled(false);
        supprimerClient.setEnabled(false);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                String mot = search.getText();
                controler.search(mot);

            }
        });
        //Progress
        initProgress.setValue(20);

        search_produit.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                System.out.println(".changedUpdate()");
                String mot = search_produit.getText();
                controler.searchProduit(mot);

            }
        });

        searchClient.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controler.searchClient(searchClient.getText());
            }
        });
        
        
        //Progress
        initProgress.setValue(35);
        
    }

    private static boolean ClientFilter(Client cl, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getClientDisplayText(cl).toLowerCase()
                .contains(textToFilter.toLowerCase());

    }

    public void configureCombo() {
        //Parametrage du comboboxClient Au niveau de la liste des ventes 
        ComboBoxFilterDecorator<Client> decorator = ComboBoxFilterDecorator.decorate(comboClients,
                CustomComboRenderer::getClientDisplayText, Fenetre::ClientFilter);

        comboClients.setRenderer(new CustomComboRenderer(decorator.getFilterTextSupplier()));

        //Parametrage du comboClient au niveau de l'interface de vendre
        ComboBoxFilterDecorator<Client> decorator1 = ComboBoxFilterDecorator.decorate(comboClients1,
                CustomComboRenderer::getClientDisplayText, Fenetre::ClientFilter);
        comboClients1.setRenderer(new CustomComboRenderer(decorator1.getFilterTextSupplier()));

        ventes.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = ventes.getSelectedRow();

                if (index >= 0) {
                    int id = Integer.parseInt(ventes.getValueAt(index, 0).toString());
                    controler.wrapDetailVenteListe(id);

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        creerCompte = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        split = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        search_produit = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        liste_prod = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        retirer = new javax.swing.JButton();
        vendre = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        comboClients1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        totale = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panier = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        supprimer = new javax.swing.JButton();
        modifier = new javax.swing.JButton();
        search = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        ajouter = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        produits = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        ajouterClient = new javax.swing.JButton();
        supprimerClient = new javax.swing.JButton();
        modifierClient = new javax.swing.JButton();
        searchClient = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        clients = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        comboClients = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        vFilter = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ventes = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        annulerVente = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        listeDetail = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        approPan = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        carder = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        BT_bilan_Stock = new javax.swing.JButton();
        BT_bilanVentes = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        accueil = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(236, 236, 236));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 204));
        jLabel1.setFont(new java.awt.Font("Verdana", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/findx.png"))); // NOI18N
        jLabel1.setToolTipText("");

        creerCompte.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        creerCompte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8_Plus_32px.png"))); // NOI18N
        creerCompte.setText("Créer un compte");
        creerCompte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creerCompteActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8_Disconnected_32px.png"))); // NOI18N
        jButton2.setText("Deconnexion");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 753, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(creerCompte, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(creerCompte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(236, 236, 236));
        jPanel4.setLayout(new java.awt.BorderLayout());

        tabbedPane.setBackground(new java.awt.Color(236, 236, 236));
        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPane.setToolTipText("Gestion des stocks");
        tabbedPane.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        tabbedPane.setName(""); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setBackground(new java.awt.Color(53, 105, 123));
        jLabel2.setFont(new java.awt.Font("Algerian", 0, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("GESTION DES VENTES");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel10.setLayout(new java.awt.BorderLayout());

        split.setDividerLocation(600);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(244, 242, 242));

        search_produit.setFont(new java.awt.Font("Serif", 0, 24)); // NOI18N
        search_produit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        search_produit.setMaximumSize(null);

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/search2.png"))); // NOI18N
        jLabel3.setLabelFor(search_produit);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/clean.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_produit, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(search_produit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setMaximumSize(null);
        jScrollPane1.setMinimumSize(null);
        jScrollPane1.setName(""); // NOI18N

        liste_prod.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        liste_prod.setToolTipText("");
        jScrollPane1.setViewportView(liste_prod);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        split.setLeftComponent(jPanel3);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(42, 92, 117));

        retirer.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        retirer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/retirer.png"))); // NOI18N
        retirer.setText("Retirer");
        retirer.setToolTipText("Retirer le produit selectionner");
        retirer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retirerActionPerformed(evt);
            }
        });

        vendre.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        vendre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/vendre.png"))); // NOI18N
        vendre.setText("VENDRE");
        vendre.setToolTipText("Valider la vente");
        vendre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vendreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(155, Short.MAX_VALUE)
                .addComponent(retirer, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(vendre, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(retirer, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendre, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jPanel19.setBackground(new java.awt.Color(244, 242, 242));

        comboClients1.setEditable(true);
        comboClients1.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        comboClients1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboClients1FocusGained(evt);
            }
        });
        comboClients1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClients1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        jLabel10.setText("CLIENT :");

        jButton3.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/addclientsm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboClients1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(comboClients1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(13, 90, 222));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/cart.png"))); // NOI18N

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(41, 177, 41));
        jLabel5.setText("Total :");

        totale.setBackground(new java.awt.Color(165, 165, 37));
        totale.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        totale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totale.setText("0");
        totale.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        totale.setOpaque(true);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totale, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(totale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        panier.setBorder(null);
        panier.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        panier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Designation", "Prix unitaire", "Quantite", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        panier.setGridColor(new java.awt.Color(13, 142, 68));
        panier.setRowHeight(28);
        panier.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                panierFocusLost(evt);
            }
        });
        panier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panierMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(panier);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        split.setRightComponent(jPanel5);

        jPanel10.add(split, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Vendre", new javax.swing.ImageIcon(getClass().getResource("/media/caisse.png")), jPanel6); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(215, 216, 216));

        supprimer.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        supprimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/deleter.png"))); // NOI18N
        supprimer.setText("Supprimer");
        supprimer.setToolTipText("Supprimer un produit");
        supprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerActionPerformed(evt);
            }
        });

        modifier.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        modifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/editrox.png"))); // NOI18N
        modifier.setText("Editer");
        modifier.setToolTipText("Modifier un produit déja existant");
        modifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierActionPerformed(evt);
            }
        });

        search.setFont(new java.awt.Font("Cambria", 0, 23)); // NOI18N
        search.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        search.setToolTipText("Chercher un produit");
        search.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                searchInputMethodTextChanged(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/search2.png"))); // NOI18N
        jLabel6.setLabelFor(search);
        jLabel6.setAutoscrolls(true);

        ajouter.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        ajouter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/addrow.png"))); // NOI18N
        ajouter.setText("Ajouter");
        ajouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/approvis.png"))); // NOI18N
        jButton1.setText("Approvisionner");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(modifier, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supprimer, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(modifier, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(supprimer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel7.add(jPanel14, java.awt.BorderLayout.PAGE_START);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        produits.setFont(new java.awt.Font("Cambria", 0, 22)); // NOI18N
        produits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        produits.setToolTipText("Liste des produits disponibles");
        produits.setGridColor(new java.awt.Color(51, 51, 51));
        produits.setRowHeight(30);
        jScrollPane3.setViewportView(produits);

        jPanel7.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Produits", new javax.swing.ImageIcon(getClass().getResource("/media/vendre.png")), jPanel7); // NOI18N

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(229, 230, 231));

        ajouterClient.setBackground(new java.awt.Color(204, 204, 204));
        ajouterClient.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        ajouterClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/addclientsm.png"))); // NOI18N
        ajouterClient.setText("Ajouter");
        ajouterClient.setToolTipText("Ajouter un nouveau produit");
        ajouterClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterClientActionPerformed(evt);
            }
        });

        supprimerClient.setBackground(new java.awt.Color(0, 153, 204));
        supprimerClient.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        supprimerClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/deleter.png"))); // NOI18N
        supprimerClient.setText("Supprimer");
        supprimerClient.setToolTipText("Supprimer un produit");
        supprimerClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerClientActionPerformed(evt);
            }
        });

        modifierClient.setBackground(new java.awt.Color(204, 204, 204));
        modifierClient.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        modifierClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/editrox.png"))); // NOI18N
        modifierClient.setText("Modifier");
        modifierClient.setToolTipText("Modifier un produit déja existant");
        modifierClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierClientActionPerformed(evt);
            }
        });

        searchClient.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        searchClient.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchClient.setToolTipText("Chercher un produit");
        searchClient.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(155, 153, 153), 1, true));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/search2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)))
                .addComponent(ajouterClient, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(modifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supprimerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ajouterClient, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(modifierClient, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(supprimerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addComponent(searchClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel8.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        clients.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        clients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Identifoant", "Nom", "Prenom", "Adresse", "Contact"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        clients.setToolTipText("Liste des produits disponibles");
        clients.setGridColor(new java.awt.Color(51, 51, 51));
        clients.setRowHeight(30);
        jScrollPane4.setViewportView(clients);

        jPanel8.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Clients", new javax.swing.ImageIcon(getClass().getResource("/media/client1.png")), jPanel8); // NOI18N

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(245, 242, 242));
        jPanel16.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel24.setBackground(new java.awt.Color(232, 232, 232));
        jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel24.setOpaque(false);

        comboClients.setEditable(true);
        comboClients.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        comboClients.setForeground(new java.awt.Color(19, 131, 181));
        comboClients.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboClientsFocusGained(evt);
            }
        });
        comboClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClientsActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        jLabel8.setText("Client :");

        vFilter.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        vFilter.setText("Filtrer");
        vFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vFilterActionPerformed(evt);
            }
        });

        refresh.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        refresh.setText("Rafraichir");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        jLabel13.setText("Date :");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboClients, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(vFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboClients)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(650);

        jPanel20.setLayout(new java.awt.BorderLayout());

        ventes.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        ventes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero Vente", "Nom client", "Prenom client", "Numero client", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ventes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ventesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(ventes);

        jPanel20.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel22.setBackground(new java.awt.Color(235, 233, 233));
        jPanel22.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Serif", 0, 28)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 102));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Liste des ventes");

        annulerVente.setFont(new java.awt.Font("Serif", 0, 15)); // NOI18N
        annulerVente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/supprimer.png"))); // NOI18N
        annulerVente.setText("Annuler la vente");
        annulerVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerVenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(annulerVente, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(annulerVente)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(jPanel20);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        listeDetail.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        listeDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Code", "Designation", "Prix Unitaire", "Quantite", "Totale"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        listeDetail.setColumnSelectionAllowed(true);
        jScrollPane6.setViewportView(listeDetail);
        listeDetail.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanel23.setBackground(new java.awt.Color(241, 235, 235));
        jPanel23.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel23.setMinimumSize(new java.awt.Dimension(6, 6));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Serif", 0, 28)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 102));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Panier de la vente");
        jLabel11.setMinimumSize(new java.awt.Dimension(220, 29));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel21);

        jPanel9.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Ventes", new javax.swing.ImageIcon(getClass().getResource("/media/ventes.png")), jPanel9, "HISTORIQUE DES VENTES"); // NOI18N

        approPan.setLayout(new java.awt.BorderLayout());
        tabbedPane.addTab("Approvisionner", new javax.swing.ImageIcon(getClass().getResource("/media/approv.png")), approPan, "HISTORIQUE DES APPROVISIONNEMENTS"); // NOI18N

        jPanel25.setLayout(new java.awt.BorderLayout());

        carder.setLayout(new java.awt.CardLayout());

        jPanel17.setBackground(new java.awt.Color(234, 234, 236));

        BT_bilan_Stock.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        BT_bilan_Stock.setText("Bilan du stock");
        BT_bilan_Stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_bilan_StockActionPerformed(evt);
            }
        });

        BT_bilanVentes.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        BT_bilanVentes.setText("Bilan des ventes");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(359, Short.MAX_VALUE)
                .addComponent(BT_bilan_Stock, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(BT_bilanVentes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(359, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(285, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_bilan_Stock, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BT_bilanVentes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(285, Short.MAX_VALUE))
        );

        carder.add(jPanel17, "FIRST");
        jPanel17.getAccessibleContext().setAccessibleName("");

        jPanel25.add(carder, java.awt.BorderLayout.CENTER);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));

        accueil.setFont(new java.awt.Font("Cambria", 0, 20)); // NOI18N
        accueil.setText("Accueil");
        accueil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accueilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accueil, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(978, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accueil, javax.swing.GroupLayout.PREFERRED_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel25.add(jPanel26, java.awt.BorderLayout.PAGE_START);

        tabbedPane.addTab("BILANS", new javax.swing.ImageIcon(getClass().getResource("/media/chart2.png")), jPanel25, ""); // NOI18N

        jPanel4.add(tabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(236, 236, 236));

        jMenu1.setText("Fichier");

        jMenuItem4.setText("Quitter");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edition");

        jMenuItem2.setText("Créer un client");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Ajouter un produit");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Aide");

        jMenuItem1.setText("A propos");
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClientsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboClientsActionPerformed

    private void comboClientsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboClientsFocusGained

    }//GEN-LAST:event_comboClientsFocusGained

    private void supprimerClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerClientActionPerformed

        int id = clients.getSelectedRow();
        if (id >= 0) {
            String code = clients.getValueAt(id, 0).toString();
            controler.removeClient(code);
            supprimerClient.setEnabled(false);
        }
    }//GEN-LAST:event_supprimerClientActionPerformed

    private void ajouterClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterClientActionPerformed
        System.out.println("spirit.Fenetre.ajouterClientActionPerformed()");
        Client_adder cl = new Client_adder(this, "Ajout de client", true);
        Client client = cl.showMe();
        if (client != null) {
            controler.addClient(client);
        }
    }//GEN-LAST:event_ajouterClientActionPerformed

    private void searchInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_searchInputMethodTextChanged

    }//GEN-LAST:event_searchInputMethodTextChanged

    private void supprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerActionPerformed
        System.out.println("spirit.Fenetre.supprimerActionPerformed()");
        int id = produits.getSelectedRow();
        if (id >= 0) {
            String code = produits.getValueAt(id, 0).toString();
            System.out.println(code);
            controler.removeProduit(code);
            supprimer.setEnabled(false);
        }
    }//GEN-LAST:event_supprimerActionPerformed

    private void panierFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panierFocusLost

    }//GEN-LAST:event_panierFocusLost

    private void comboClients1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClients1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboClients1ActionPerformed

    private void comboClients1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboClients1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_comboClients1FocusGained

    private void vendreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vendreActionPerformed
        Client cl = (Client) comboClients1.getSelectedItem();
        if (cl != null) {
            if (controler.getPanier().size() > 0 && controler.verifierPanier()) {

                Vente vente = new Vente(0, null, controler.getPanier(), cl);

                controler.vendre(vente, 2000);
                System.out.println("La vente a ete effectuer avec succes !");

            } else {
                JOptionPane.showMessageDialog(null, "La quantité de certains produit du panier est invalide");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un clien d'abord");
        }
    }//GEN-LAST:event_vendreActionPerformed

    private void retirerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retirerActionPerformed
        int select = panier.getSelectedRow();
        if (select >= 0) {
            int reponse = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment retirer cette ligne du panier ?", "Attention !", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (reponse == JOptionPane.YES_OPTION) {
                controler.removeFromPanier(controler.getPanier().get(select));
                retirer.setEnabled(false);
            }

        }
    }//GEN-LAST:event_retirerActionPerformed

    private void annulerVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerVenteActionPerformed
        int index = ventes.getSelectedRow();
        if (index >= 0) {
            int id = Integer.parseInt(ventes.getValueAt(index, 0).toString());
            System.out.println("L'identifiant de la vente selectionner est : " + id);

            int reponse = JOptionPane.showConfirmDialog(null, "Voulez vous vraiment annuler cette vente ?");
            if (reponse == JOptionPane.YES_OPTION) {
                if (controler.annulerVente(id)) {
                    JOptionPane.showMessageDialog(this, "La vente a été annuler avec succès !", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Un problème est survenu lors de l'annulation la vente n'a pas été annuler", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_annulerVenteActionPerformed

    private void ajouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterActionPerformed
        Produit_adder adder = new Produit_adder(this, "Ajout de produit", true);
        Produit p = adder.showMe();
        if (p != null) {
            controler.addProduit(p);
        }
        System.out.println("spirit.Fenetre.ajouterActionPerformed()");
    }//GEN-LAST:event_ajouterActionPerformed

    private void modifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierActionPerformed
        int select = produits.getSelectedRow();
        if (select >= 0) {

            String code = produits.getValueAt(select, 0).toString();

            ProduitModify pm = new ProduitModify(null, "Modification", true);
            Produit pr = pm.showMe(code);

            if (pr != null) {
                controler.modifierProduit(pr);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner une ligne dans le tableau");
        }
    }//GEN-LAST:event_modifierActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int select = produits.getSelectedRow();
        if (select >= 0) {
            String str = JOptionPane.showInputDialog("Entrer la quantité à ajouter");
            if (str != null && str.length() > 0) {
                try {

                    String code = produits.getValueAt(select, 0).toString();
                    controler.updateStock(Integer.parseInt(str), code);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Valeur incorrecte !");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selectionner un produit dans le tableau d'abord !");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void vFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vFilterActionPerformed
        Client client = (Client) comboClients.getSelectedItem();
        Date mDate = date.getDate();
        if (mDate != null && client != null) {
            controler.searchVente(mDate, client.getId());
        } else if (mDate != null) {

            updateVentesProduits(controler.getVmanager().search(mDate));

        } else if (client != null) {

            updateVentesProduits(controler.getVmanager().search(client.getId()));

        }
    }//GEN-LAST:event_vFilterActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        updateVentesProduits(controler.getVmanager().findAll());
    }//GEN-LAST:event_refreshActionPerformed

    private void ventesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ventesMouseClicked
        annulerVente.setEnabled(true);
    }//GEN-LAST:event_ventesMouseClicked

    private void creerCompteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creerCompteActionPerformed
        Compte cmp = new Compte(this, "Ajout d'utilisateur", true);

        MPoste pst = cmp.showMe();
        if (pst != null) {
            PManager pm = new PManager();
            pm.create(pst);
        } else {
            JOptionPane.showMessageDialog(null, "Le compte n'a pas été créer !");
        }

    }//GEN-LAST:event_creerCompteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void modifierClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierClientActionPerformed
        int selected = clients.getSelectedRow();
        if (selected >= 0) {

            Client_Modifier mod = new Client_Modifier(null, "Modifier les données", true);
            Client client = mod.showMe((Client) clients.getValueAt(selected, 0));

            if (client != null) {
                controler.updateClient(client);
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Selectionner un client d'abord !");
        }
    }//GEN-LAST:event_modifierClientActionPerformed

    private void accueilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accueilActionPerformed
        CardLayout layout = (CardLayout) carder.getLayout();
        layout.show(carder, "FIRST");
        accueil.setEnabled(false);
    }//GEN-LAST:event_accueilActionPerformed

    private void BT_bilan_StockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_bilan_StockActionPerformed

    }//GEN-LAST:event_BT_bilan_StockActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        search_produit.setText("");
        controler.search("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void panierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panierMouseClicked

    }//GEN-LAST:event_panierMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        System.exit(0);     // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_bilanVentes;
    private javax.swing.JButton BT_bilan_Stock;
    private javax.swing.JButton accueil;
    private javax.swing.JButton ajouter;
    private javax.swing.JButton ajouterClient;
    private javax.swing.JButton annulerVente;
    private javax.swing.JPanel approPan;
    private javax.swing.JPanel carder;
    private javax.swing.JTable clients;
    private javax.swing.JComboBox<Client> comboClients;
    private javax.swing.JComboBox<Client> comboClients1;
    private javax.swing.JButton creerCompte;
    private com.toedter.calendar.JDateChooser date;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable listeDetail;
    private javax.swing.JList<spirit.entites.Produit> liste_prod;
    private javax.swing.JButton modifier;
    private javax.swing.JButton modifierClient;
    private javax.swing.JTable panier;
    private javax.swing.JTable produits;
    private javax.swing.JButton refresh;
    private javax.swing.JButton retirer;
    private javax.swing.JTextField search;
    private javax.swing.JTextField searchClient;
    private javax.swing.JTextField search_produit;
    private javax.swing.JSplitPane split;
    private javax.swing.JButton supprimer;
    private javax.swing.JButton supprimerClient;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel totale;
    private javax.swing.JButton vFilter;
    private javax.swing.JButton vendre;
    private javax.swing.JTable ventes;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateClient(ArrayList<Client> liste) {

        ((DefaultTableModel) clients.getModel()).setRowCount(0);

        comboClients.removeAllItems();
        comboClients1.removeAllItems();

        for (Client p : liste) {

            ((DefaultTableModel) clients.getModel()).addRow(
                    new Object[]{
                        p,
                        p.getNom(),
                        p.getPrenom(),
                        p.getAdresse(),
                        p.getContact()
                    }
            );

            comboClients.addItem(p);
            comboClients1.addItem(p);
        }

    }

    @Override
    public void updateProduit(ArrayList<Produit> liste) {

        ((DefaultTableModel) produits.getModel()).setRowCount(0);

        for (Produit p : liste) {
            String[] line = {
                p.getCode(),
                p.getDesignation(),
                String.valueOf(p.getPrix()),
                String.valueOf(p.getStock())
            };

            ((DefaultTableModel) produits.getModel()).addRow(line);
        }

    }

    public void manageCard() {
        
        carder.add("VENTES", new PanneauBilan());
        carder.add("STOCK", new JPanel());

        BT_bilanVentes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) carder.getLayout();
                layout.show(carder, "VENTES");
                accueil.setEnabled(true);
            }
        });

        BT_bilan_Stock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) carder.getLayout();
                layout.show(carder, "STOCK");
                accueil.setEnabled(true);
            }
        });
    }

    @Override
    public void updateVentesProduits(ArrayList<Vente> listeList) {
        ((DefaultTableModel) ventes.getModel()).setRowCount(0);
        ((DefaultTableModel) listeDetail.getModel()).setRowCount(0);

        System.out.println(listeList.size());
        for (Vente ve : listeList) {
            String[] ligne = {
                String.valueOf(ve.getId()),
                ve.getClient().getNom(),
                ve.getClient().getPrenom(),
                ve.getClient().getId(),
                ve.getDate().toString()};

            ((DefaultTableModel) ventes.getModel()).addRow(ligne);
        }

        annulerVente.setEnabled(false);
    }

    @Override
    public void updatePanier(ArrayList<DetailVente> ps) {
        ((DefaultTableModel) panier.getModel()).setRowCount(0);

        double vtotale = 0;

        for (DetailVente dt : ps) {
            String[] tab = {
                dt.getProduit().getDesignation(),
                String.valueOf(Math.round(dt.getProduit().getPrix())),
                String.valueOf(dt.getQuantite()),
                String.valueOf(dt.getTotal())
            };

            ((DefaultTableModel) panier.getModel()).addRow(tab);

            vtotale += dt.getTotal();
        }

        System.out.println(controler.verifierPanier());

        if (controler.verifierPanier()) {
            vendre.setEnabled(true);
        } else {
            vendre.setEnabled(false);
        }
        totale.setText(NumberFormat.getInstance().format(Math.round(vtotale)) + " FCFA");
    }

    @Override
    public void updateVenteUIproduit(ArrayList<Produit> list) {

        DefaultListModel<Produit> model = new DefaultListModel<>();

        for (Produit p : list) {
            model.addElement(p);
        }
        liste_prod.setModel(model);

    }

    @Override
    public void updateDetailVente(ArrayList<DetailVente> liste) {

        ((DefaultTableModel) listeDetail.getModel()).setRowCount(0);

        for (DetailVente dt : liste) {
            String[] line = {dt.getProduit().getCode(), dt.getProduit().getDesignation(),
                String.valueOf(Math.round(dt.getProduit().getPrix())), String.valueOf(dt.getQuantite()),
                String.valueOf(Math.round(dt.getTotal()))};

            ((DefaultTableModel) listeDetail.getModel()).addRow(line);
        }

    }

    public void userAdapder(MPoste use) {
        this.user = use;

        if (user != null) {
            if (user.getPoste() == MPoste.Poste.VENDEUR) {
                supprimer.setVisible(false);
                modifier.setVisible(false);
                supprimerClient.setVisible(false);
                modifierClient.setVisible(false);
                creerCompte.setVisible(false);
                tabbedPane.remove(4);
                BT_bilanVentes.setEnabled(false);
                BT_bilan_Stock.setEnabled(false);
            }
        }
    }

    private void initPopUp() {

        MyListener listener = new MyListener();

        pOne.addActionListener(listener);
        pTwo.addActionListener(listener);
        mOne.addActionListener(listener);
        mTwo.addActionListener(listener);

        jpm.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                int selected = liste_prod.getSelectedIndex();

                if (selected >= 0) {
                    jpm.removeAll();
                    Produit pr = liste_prod.getSelectedValue();
                    if (controler.existe(pr.getCode()) < 0) {
                        jpm.add(pOne);
                        jpm.add(pTwo);
                    } else {
                        jpm.add(pOne);
                        jpm.add(pTwo);
                        jpm.add(mOne);
                        jpm.add(mTwo);
                        jpm.addSeparator();
                    }
                }

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        liste_prod.setComponentPopupMenu(jpm);

    }

    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();

            if (liste_prod.getSelectedIndex() >= 0) {
                Produit produit = liste_prod.getSelectedValue();
                DetailVente dtv = new DetailVente(0, produit.getPrix(), null, produit);

                if (item == pOne) {
                    controler.addToPanier(1, dtv);
                } else if (item == pTwo) {
                    controler.addToPanier(2, dtv);
                } else if (item == mOne) {
                    controler.addToPanier(-1, dtv);
                } else if (item == mTwo) {
                    controler.addToPanier(-2, dtv);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucun produit selectionner !");
            }
        }

    }
    
    private class CellEditListener implements CellEditorListener{

        @Override
        public void editingStopped(ChangeEvent e) {

            int col = panier.getSelectedColumn();
            int row = panier.getSelectedRow();

            if (col == 1) {

                try {
                    long prix_unitaire = Long.parseLong(panier.getValueAt(row, col).toString());
                    if (prix_unitaire > 0 ) {
                        controler.getPanier().get(row).setPrixnegocier(prix_unitaire);
                    }else{
                        JOptionPane.showMessageDialog(null, "Attention prix invalide !");
                    }

                } catch (NumberFormatException ex) {
                    System.err.println(ex.getMessage());
                }finally{
                    updatePanier(controler.getPanier());
                }

            }else if(col == 2){

                 try{
                     long quantite = Long.parseLong(panier.getValueAt(row, col).toString());
                    DetailVente dv = controler.getPanier().get(row);
                    if (dv.getProduit().getStock() >= quantite) {
                        
                        dv.setQuantite((int)quantite);
                        
                    }else{
                        JOptionPane.showMessageDialog(null, "Attention pas assez de stock !");
                    }
                 }catch(Exception ex){
                     System.out.println(ex.getMessage());
                 }finally{
                      updatePanier(controler.getPanier());
                 }

            }

        }

        @Override
        public void editingCanceled(ChangeEvent e) {

        }
        
    }

}
