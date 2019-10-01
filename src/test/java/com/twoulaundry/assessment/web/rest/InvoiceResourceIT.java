package com.twoulaundry.assessment.web.rest;

import com.twoulaundry.assessment.Application;
import com.twoulaundry.assessment.domain.Invoice;
import com.twoulaundry.assessment.repository.InvoiceRepository;
import com.twoulaundry.assessment.service.InvoiceService;
import com.twoulaundry.assessment.service.dto.InvoiceDTO;
import com.twoulaundry.assessment.service.mapper.InvoiceMapper;
import com.twoulaundry.assessment.web.rest.errors.ExceptionTranslator;
import com.twoulaundry.assessment.service.dto.InvoiceCriteria;
import com.twoulaundry.assessment.service.InvoiceQueryService;

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

import static com.twoulaundry.assessment.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.twoulaundry.assessment.domain.enumeration.Currency;
import com.twoulaundry.assessment.domain.enumeration.InvoiceStatus;
/**
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "68";
    private static final String UPDATED_INVOICE_NUMBER = "07";

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
    private InvoiceQueryService invoiceQueryService;

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
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
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
            .invoice_number(DEFAULT_INVOICE_NUMBER)
            .total(DEFAULT_TOTAL)
            .currency(DEFAULT_CURRENCY)
            .invoice_date(DEFAULT_INVOICE_DATE)
            .due_date(DEFAULT_DUE_DATE)
            .vendor_name(DEFAULT_VENDOR_NAME)
            .remittance_address(DEFAULT_REMITTANCE_ADDRESS)
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
            .invoice_number(UPDATED_INVOICE_NUMBER)
            .total(UPDATED_TOTAL)
            .currency(UPDATED_CURRENCY)
            .invoice_date(UPDATED_INVOICE_DATE)
            .due_date(UPDATED_DUE_DATE)
            .vendor_name(UPDATED_VENDOR_NAME)
            .remittance_address(UPDATED_REMITTANCE_ADDRESS)
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
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoice_number()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testInvoice.getInvoice_date()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getDue_date()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoice.getVendor_name()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testInvoice.getRemittance_address()).isEqualTo(DEFAULT_REMITTANCE_ADDRESS);
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
    public void checkInvoice_numberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoice_number(null);

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
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTotal(null);

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
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCurrency(null);

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
    public void checkInvoice_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoice_date(null);

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
            .andExpect(jsonPath("$.[*].invoice_number").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].invoice_date").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].due_date").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].vendor_name").value(hasItem(DEFAULT_VENDOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].remittance_address").value(hasItem(DEFAULT_REMITTANCE_ADDRESS.toString())))
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
            .andExpect(jsonPath("$.invoice_number").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.invoice_date").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.due_date").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.vendor_name").value(DEFAULT_VENDOR_NAME.toString()))
            .andExpect(jsonPath("$.remittance_address").value(DEFAULT_REMITTANCE_ADDRESS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_numberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_number equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoice_number.equals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoice_number equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoice_number.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_numberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_number in DEFAULT_INVOICE_NUMBER or UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoice_number.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER);

        // Get all the invoiceList where invoice_number equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoice_number.in=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_numberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_number is not null
        defaultInvoiceShouldBeFound("invoice_number.specified=true");

        // Get all the invoiceList where invoice_number is null
        defaultInvoiceShouldNotBeFound("invoice_number.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total equals to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is not null
        defaultInvoiceShouldBeFound("total.specified=true");

        // Get all the invoiceList where total is null
        defaultInvoiceShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than or equal to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than or equal to SMALLER_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than SMALLER_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }


    @Test
    @Transactional
    public void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency equals to DEFAULT_CURRENCY
        defaultInvoiceShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultInvoiceShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllInvoicesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency is not null
        defaultInvoiceShouldBeFound("currency.specified=true");

        // Get all the invoiceList where currency is null
        defaultInvoiceShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoice_date equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the invoiceList where invoice_date equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date is not null
        defaultInvoiceShouldBeFound("invoice_date.specified=true");

        // Get all the invoiceList where invoice_date is null
        defaultInvoiceShouldNotBeFound("invoice_date.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date is greater than or equal to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.greaterThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoice_date is greater than or equal to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.greaterThanOrEqual=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date is less than or equal to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.lessThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoice_date is less than or equal to SMALLER_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.lessThanOrEqual=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date is less than DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoice_date is less than UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.lessThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoice_dateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoice_date is greater than DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoice_date.greaterThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoice_date is greater than SMALLER_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoice_date.greaterThan=" + SMALLER_INVOICE_DATE);
    }


    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date equals to DEFAULT_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.equals=" + DEFAULT_DUE_DATE);

        // Get all the invoiceList where due_date equals to UPDATED_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the invoiceList where due_date equals to UPDATED_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date is not null
        defaultInvoiceShouldBeFound("due_date.specified=true");

        // Get all the invoiceList where due_date is null
        defaultInvoiceShouldNotBeFound("due_date.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date is greater than or equal to DEFAULT_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the invoiceList where due_date is greater than or equal to UPDATED_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date is less than or equal to DEFAULT_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the invoiceList where due_date is less than or equal to SMALLER_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date is less than DEFAULT_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the invoiceList where due_date is less than UPDATED_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDue_dateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where due_date is greater than DEFAULT_DUE_DATE
        defaultInvoiceShouldNotBeFound("due_date.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the invoiceList where due_date is greater than SMALLER_DUE_DATE
        defaultInvoiceShouldBeFound("due_date.greaterThan=" + SMALLER_DUE_DATE);
    }


    @Test
    @Transactional
    public void getAllInvoicesByVendor_nameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vendor_name equals to DEFAULT_VENDOR_NAME
        defaultInvoiceShouldBeFound("vendor_name.equals=" + DEFAULT_VENDOR_NAME);

        // Get all the invoiceList where vendor_name equals to UPDATED_VENDOR_NAME
        defaultInvoiceShouldNotBeFound("vendor_name.equals=" + UPDATED_VENDOR_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVendor_nameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vendor_name in DEFAULT_VENDOR_NAME or UPDATED_VENDOR_NAME
        defaultInvoiceShouldBeFound("vendor_name.in=" + DEFAULT_VENDOR_NAME + "," + UPDATED_VENDOR_NAME);

        // Get all the invoiceList where vendor_name equals to UPDATED_VENDOR_NAME
        defaultInvoiceShouldNotBeFound("vendor_name.in=" + UPDATED_VENDOR_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVendor_nameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vendor_name is not null
        defaultInvoiceShouldBeFound("vendor_name.specified=true");

        // Get all the invoiceList where vendor_name is null
        defaultInvoiceShouldNotBeFound("vendor_name.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByRemittance_addressIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remittance_address equals to DEFAULT_REMITTANCE_ADDRESS
        defaultInvoiceShouldBeFound("remittance_address.equals=" + DEFAULT_REMITTANCE_ADDRESS);

        // Get all the invoiceList where remittance_address equals to UPDATED_REMITTANCE_ADDRESS
        defaultInvoiceShouldNotBeFound("remittance_address.equals=" + UPDATED_REMITTANCE_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByRemittance_addressIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remittance_address in DEFAULT_REMITTANCE_ADDRESS or UPDATED_REMITTANCE_ADDRESS
        defaultInvoiceShouldBeFound("remittance_address.in=" + DEFAULT_REMITTANCE_ADDRESS + "," + UPDATED_REMITTANCE_ADDRESS);

        // Get all the invoiceList where remittance_address equals to UPDATED_REMITTANCE_ADDRESS
        defaultInvoiceShouldNotBeFound("remittance_address.in=" + UPDATED_REMITTANCE_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByRemittance_addressIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remittance_address is not null
        defaultInvoiceShouldBeFound("remittance_address.specified=true");

        // Get all the invoiceList where remittance_address is null
        defaultInvoiceShouldNotBeFound("remittance_address.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status equals to DEFAULT_STATUS
        defaultInvoiceShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the invoiceList where status equals to UPDATED_STATUS
        defaultInvoiceShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInvoiceShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the invoiceList where status equals to UPDATED_STATUS
        defaultInvoiceShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where status is not null
        defaultInvoiceShouldBeFound("status.specified=true");

        // Get all the invoiceList where status is null
        defaultInvoiceShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoice_number").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].invoice_date").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].due_date").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].vendor_name").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].remittance_address").value(hasItem(DEFAULT_REMITTANCE_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restInvoiceMockMvc.perform(get("/api/invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc.perform(get("/api/invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
            .invoice_number(UPDATED_INVOICE_NUMBER)
            .total(UPDATED_TOTAL)
            .currency(UPDATED_CURRENCY)
            .invoice_date(UPDATED_INVOICE_DATE)
            .due_date(UPDATED_DUE_DATE)
            .vendor_name(UPDATED_VENDOR_NAME)
            .remittance_address(UPDATED_REMITTANCE_ADDRESS)
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
        assertThat(testInvoice.getInvoice_number()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getInvoice_date()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getDue_date()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoice.getVendor_name()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testInvoice.getRemittance_address()).isEqualTo(UPDATED_REMITTANCE_ADDRESS);
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
