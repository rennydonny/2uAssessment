package com.twoulaundry.assessment.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.twoulaundry.assessment.domain.enumeration.Currency;
import com.twoulaundry.assessment.domain.enumeration.InvoiceStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.twoulaundry.assessment.domain.Invoice} entity. This class is used
 * in {@link com.twoulaundry.assessment.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {
        }

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }

    }
    /**
     * Class for filtering InvoiceStatus
     */
    public static class InvoiceStatusFilter extends Filter<InvoiceStatus> {

        public InvoiceStatusFilter() {
        }

        public InvoiceStatusFilter(InvoiceStatusFilter filter) {
            super(filter);
        }

        @Override
        public InvoiceStatusFilter copy() {
            return new InvoiceStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invoiceNumber;

    private BigDecimalFilter total;

    private CurrencyFilter currency;

    private LocalDateFilter invoiceDate;

    private LocalDateFilter dueDate;

    private StringFilter vendorName;

    private StringFilter remittanceAddress;

    private InvoiceStatusFilter status;

    public InvoiceCriteria(){
    }

    public InvoiceCriteria(InvoiceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.invoiceNumber = other.invoiceNumber == null ? null : other.invoiceNumber.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.invoiceDate = other.invoiceDate == null ? null : other.invoiceDate.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.vendorName = other.vendorName == null ? null : other.vendorName.copy();
        this.remittanceAddress = other.remittanceAddress == null ? null : other.remittanceAddress.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public LocalDateFilter getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public StringFilter getVendorName() {
        return vendorName;
    }

    public void setVendorName(StringFilter vendorName) {
        this.vendorName = vendorName;
    }

    public StringFilter getRemittanceAddress() {
        return remittanceAddress;
    }

    public void setRemittanceAddress(StringFilter remittanceAddress) {
        this.remittanceAddress = remittanceAddress;
    }

    public InvoiceStatusFilter getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatusFilter status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(total, that.total) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(vendorName, that.vendorName) &&
            Objects.equals(remittanceAddress, that.remittanceAddress) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        invoiceNumber,
        total,
        currency,
        invoiceDate,
        dueDate,
        vendorName,
        remittanceAddress,
        status
        );
    }

    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (invoiceNumber != null ? "invoiceNumber=" + invoiceNumber + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (currency != null ? "currency=" + currency + ", " : "") +
                (invoiceDate != null ? "invoiceDate=" + invoiceDate + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (vendorName != null ? "vendorName=" + vendorName + ", " : "") +
                (remittanceAddress != null ? "remittanceAddress=" + remittanceAddress + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
