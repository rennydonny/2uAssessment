package com.twoulaundry.test.web.rest;

import com.twoulaundry.test.TwoulaundrytestApp;
import com.twoulaundry.test.domain.Invoice;
import com.twoulaundry.test.repository.InvoiceRepository;
import com.twoulaundry.test.service.InvoiceService;
import com.twoulaundry.test.service.dto.InvoiceDTO;
import com.twoulaundry.test.service.mapper.InvoiceMapper;
import com.twoulaundry.test.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.twoulaundry.test.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.twoulaundry.test.domain.enumeration.Currency;
import com.twoulaundry.test.domain.enumeration.InvoiceStatus;
/**
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@SpringBootTest(classes = TwoulaundrytestApp.class)
public class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "6";
    private static final String UPDATED_INVOICE_NUMBER = "88";

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(1 - 1);

    private static final Currency DEFAULT_CURRENCY = Currency.USD;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INVOICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMITTANCE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_REMITTANCE_ADDRESS = "BBBBBBBBBB";

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.PENDING;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.APPROVED;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .total(DEFAULT_TOTAL)
            .currency(DEFAULT_CURRENCY)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .vendorName(DEFAULT_VENDOR_NAME)
            .remittanceAddress(DEFAULT_REMITTANCE_ADDRESS)
            .status(DEFAULT_STATUS);
        return invoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .total(UPDATED_TOTAL)
            .currency(UPDATED_CURRENCY)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .vendorName(UPDATED_VENDOR_NAME)
            .remittanceAddress(UPDATED_REMITTANCE_ADDRESS)
            .status(UPDATED_STATUS);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoice.getVendorName()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testInvoice.getRemittanceAddress()).isEqualTo(DEFAULT_REMITTANCE_ADDRESS);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceNumber(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].remittanceAddress").value(hasItem(DEFAULT_REMITTANCE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME.toString()))
            .andExpect(jsonPath("$.remittanceAddress").value(DEFAULT_REMITTANCE_ADDRESS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .total(UPDATED_TOTAL)
            .currency(UPDATED_CURRENCY)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .vendorName(UPDATED_VENDOR_NAME)
            .remittanceAddress(UPDATED_REMITTANCE_ADDRESS)
            .status(UPDATED_STATUS);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoice.getVendorName()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testInvoice.getRemittanceAddress()).isEqualTo(UPDATED_REMITTANCE_ADDRESS);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}
