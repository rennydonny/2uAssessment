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

    private StringFilter invoice_number;

    private BigDecimalFilter total;

    private CurrencyFilter currency;

    private LocalDateFilter invoice_date;

    private LocalDateFilter due_date;

    private StringFilter vendor_name;

    private StringFilter remittance_address;

    private InvoiceStatusFilter status;

    public InvoiceCriteria(){
    }

    public InvoiceCriteria(InvoiceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.invoice_number = other.invoice_number == null ? null : other.invoice_number.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.invoice_date = other.invoice_date == null ? null : other.invoice_date.copy();
        this.due_date = other.due_date == null ? null : other.due_date.copy();
        this.vendor_name = other.vendor_name == null ? null : other.vendor_name.copy();
        this.remittance_address = other.remittance_address == null ? null : other.remittance_address.copy();
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

    public StringFilter getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(StringFilter invoice_number) {
        this.invoice_number = invoice_number;
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

    public LocalDateFilter getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(LocalDateFilter invoice_date) {
        this.invoice_date = invoice_date;
    }

    public LocalDateFilter getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDateFilter due_date) {
        this.due_date = due_date;
    }

    public StringFilter getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(StringFilter vendor_name) {
        this.vendor_name = vendor_name;
    }

    public StringFilter getRemittance_address() {
        return remittance_address;
    }

    public void setRemittance_address(StringFilter remittance_address) {
        this.remittance_address = remittance_address;
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
            Objects.equals(invoice_number, that.invoice_number) &&
            Objects.equals(total, that.total) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(invoice_date, that.invoice_date) &&
            Objects.equals(due_date, that.due_date) &&
            Objects.equals(vendor_name, that.vendor_name) &&
            Objects.equals(remittance_address, that.remittance_address) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        invoice_number,
        total,
        currency,
        invoice_date,
        due_date,
        vendor_name,
        remittance_address,
        status
        );
    }

    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (invoice_number != null ? "invoice_number=" + invoice_number + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (currency != null ? "currency=" + currency + ", " : "") +
                (invoice_date != null ? "invoice_date=" + invoice_date + ", " : "") +
                (due_date != null ? "due_date=" + due_date + ", " : "") +
                (vendor_name != null ? "vendor_name=" + vendor_name + ", " : "") +
                (remittance_address != null ? "remittance_address=" + remittance_address + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
