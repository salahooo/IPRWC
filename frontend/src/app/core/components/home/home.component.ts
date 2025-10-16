import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Observable, map } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { Product, resolveCategory } from '../../../shared/models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  products$!: Observable<Product[]>;
  featured$!: Observable<Product[]>;
  // Pre-calculated stats rendered in the hero panel
  readonly heroStats = [
    { label: 'Custom PCs delivered', value: '750+' },
    { label: 'Average service rating', value: '4.9/5' },
    { label: 'Cities served by our gaming PCs', value: '32' }
  ];
  readonly promoHighlights = [
    {
      title: 'Back to school PC bundles',
      description: 'Save on study-ready desktops when you bundle CPU, RAM, and SSD.',
      cta: 'Explore bundles',
      link: '/catalog',
      theme: 'accent',
      badge: 'Limited time'
    },
    {
      title: 'Book a build session',
      description: 'Clean builds, cable management, and thermal tuning handled by certified PC technicians.',
      cta: 'Schedule service',
      link: '/profile',
      theme: 'outline'
    },
    {
      title: 'Attend a demo day',
      description: 'See the latest GPUs, cooling, and peripherals at our Saturday in-store demos.',
      cta: 'Reserve a spot',
      link: '/auth/register',
      theme: 'dark'
    }
  ];
  readonly serviceHighlights = [
    {
      icon: 'engineering',
      title: 'Certified workshop',
      description: 'Diagnostics, BIOS updates, and custom builds finished in under 48 hours.'
    },
    {
      icon: 'memory',
      title: 'In-store demos',
      description: 'Benchmark the latest GPUs and CPUs before you buy.'
    },
    {
      icon: 'bolt',
      title: 'Performance tuning',
      description: 'Overclocking profiles, airflow optimization, and thermal paste service.'
    },
    {
      icon: 'support_agent',
      title: 'Build support',
      description: 'One-on-one consultations and compatibility checks for your parts list.'
    }
  ];
  readonly reasons = [
    {
      icon: 'workspace_premium',
      title: '2-year warranty',
      description: 'Coverage on custom builds; components per manufacturer terms.'
    },
    {
      icon: 'local_shipping',
      title: 'Same-week delivery',
      description: 'We deliver fully built PCs across the Netherlands.'
    },
    {
      icon: 'support_agent',
      title: 'Dedicated concierge',
      description: 'Direct access to our support crew via WhatsApp, mail, or phone.'
    }
  ];
  readonly visitDetails = [
    { label: 'Address', value: 'Nieuwe Binnenweg 123, Rotterdam' },
    { label: 'Workshop hours', value: 'Tue – Sat · 10:00 – 18:00' },
    { label: 'Ride-outs', value: 'Every first Sunday · 09:30' }
  ];
  readonly newsletterPerks = [
    'Early access to limited drops',
    'Maintenance reminders & tips',
    'Invites to demo days and build clinics'
  ];
  // Sanitised Google Maps iframe URL so Angular treats it as safe
  readonly mapEmbedUrl: SafeResourceUrl;

  constructor(
    private readonly productService: ProductService,
    private readonly sanitizer: DomSanitizer
  ) {
    // Pre-sanitise the embed once so change detection doesn't recompute it
    this.mapEmbedUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
      'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2436.0794684586546!2d4.470660877207305!3d51.92442067187477!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x47c4335769f57ac9%3A0xede6386e0d454893!2sRotterdam!5e0!3m2!1sen!2snl!4v1696263000000!5m2!1sen!2snl'
    );
  }

  ngOnInit(): void {
    // Cache the full list then derive the subset used by the "Popular builds" grid
    this.products$ = this.productService.list();
    this.featured$ = this.products$.pipe(
      map(products => products.slice(0, 4))
    );
  }

  categoryFor(product: Product): string {
    // Map SKUs to friendly category labels for the card badges
    return resolveCategory(product.sku);
  }
}
