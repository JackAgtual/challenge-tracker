import { Button } from "./ui/button";

export default function LogoutButton() {
  return (
    <Button>
      <a href="/auth/logout">Logout</a>
    </Button>
  );
}
