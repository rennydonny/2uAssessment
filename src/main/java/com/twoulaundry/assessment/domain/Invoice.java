package com.twoulaundry.assessment.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.twoulaundry.assessment.domain.enumeration.Currency;

import com.twoulaundry.assessment.domain.enumeration.InvoiceStatus;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]+$")
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoice_number;

    @NotNull
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoice_date;

    @Column(name = "due_date")
    private LocalDate due_date;

    @Column(name = "vendor_name")
    private String vendor_name;

    @Column(name = "remittance_address")
    private String remittance_address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public Invoice invoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
        return this;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Invoice total(BigDecimal total) {
        this.total = total;
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Invoice currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getInvoice_date() {
        return invoice_date;
    }

    public Invoice invoice_date(LocalDate invoice_date) {
        this.invoice_date = invoice_date;
        return this;
    }

    public void setInvoice_date(LocalDate invoice_date) {
        this.invoice_date = invoice_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public Invoice due_date(LocalDate due_date) {
        this.due_date = due_date;
        return this;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public Invoice vendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
        return this;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getRemittance_address() {
        return remittance_address;
    }

    public Invoice remittance_address(String remittance_address) {
        this.remittance_address = remittance_address;
        return this;
    }

    public void setRemittance_address(String remittance_address) {
        this.remittance_address = remittance_address;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Invoice status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoice_number='" + getInvoice_number() + "'" +
            ", total=" + getTotal() +
            ", currency='" + getCurrency() + "'" +
            ", invoice_date='" + getInvoice_date() + "'" +
            ", due_date='" + getDue_date() + "'" +
            ", vendor_name='" + getVendor_name() + "'" +
            ", remittance_address='" + getRemittance_address() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
