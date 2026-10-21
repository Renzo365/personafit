<?php

namespace Tests\Feature;

use Database\Seeders\DatabaseSeeder;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ExampleTest extends TestCase
{
    use RefreshDatabase;

    /**
     * Test admin dashboard page loads successfully.
     */
    public function test_the_application_redirects_and_admin_dashboard_loads(): void
    {
        $this->seed(DatabaseSeeder::class);

        $response = $this->get('/');
        $response->assertRedirect('/admin');

        $adminResponse = $this->get('/admin');
        $adminResponse->assertStatus(200)
            ->assertSee('System Dashboard')
            ->assertSee('PERSONAFIT');
    }
}
